package io.github.orionlibs.core.tests;

import static io.restassured.RestAssured.given;

import io.github.orionlibs.core.Logger;
import io.github.orionlibs.core.json.JSONService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class APITestUtils
{
    @Autowired
    private JSONService jsonService;
    @Value("${jwt.secret}")
    String base64Secret;


    private Key getSigningKey()
    {
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }


    private String jwtWithAuthorities(String subject, String... authorities)
    {
        return Jwts.builder()
                        .setSubject(subject)
                        .claim("authorities", List.of(authorities))
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                        .compact();
    }


    public Response makeGetAPICall(HttpHeaders headers)
    {
        Logger.info("[JUnit] making GET call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response();
    }


    public Response makeGetAPICall(HttpHeaders headers, String subject, String commaSeparatedAuthorities)
    {
        Logger.info("[JUnit] making GET call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .auth().oauth2(jwtWithAuthorities(subject, commaSeparatedAuthorities.split(",")))
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePostAPICall(Object objectToSave, HttpHeaders headers)
    {
        Logger.info("[JUnit] making POST call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(jsonService.toJson(objectToSave))
                        .when()
                        .post()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePostAPICall(Object objectToSave, HttpHeaders headers, String subject, String commaSeparatedAuthorities)
    {
        Logger.info("[JUnit] making POST call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .auth().oauth2(jwtWithAuthorities(subject, commaSeparatedAuthorities.split(",")))
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(jsonService.toJson(objectToSave))
                        .when()
                        .post()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePutAPICall(Object objectToSave, HttpHeaders headers)
    {
        Logger.info("[JUnit] making PUT call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(jsonService.toJson(objectToSave))
                        .when()
                        .put()
                        .then()
                        .extract()
                        .response();
    }


    public Response makePutAPICall(Object objectToSave, HttpHeaders headers, String subject, String commaSeparatedAuthorities)
    {
        Logger.info("[JUnit] making PUT call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .auth().oauth2(jwtWithAuthorities(subject, commaSeparatedAuthorities.split(",")))
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .body(jsonService.toJson(objectToSave))
                        .when()
                        .put()
                        .then()
                        .extract()
                        .response();
    }


    public Response makeDeleteAPICall(HttpHeaders headers)
    {
        Logger.info("[JUnit] making DELETE call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .when()
                        .delete()
                        .then()
                        .extract()
                        .response();
    }


    public Response makeDeleteAPICall(HttpHeaders headers, String subject, String commaSeparatedAuthorities)
    {
        Logger.info("[JUnit] making DELETE call");
        RestAssured.defaultParser = Parser.JSON;
        headers = getHttpHeaders(headers);
        return given()
                        .auth().oauth2(jwtWithAuthorities(subject, commaSeparatedAuthorities.split(",")))
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .accept(ContentType.JSON)
                        .when()
                        .delete()
                        .then()
                        .extract()
                        .response();
    }


    private HttpHeaders getHttpHeaders(HttpHeaders headers)
    {
        if(headers == null)
        {
            headers = new HttpHeaders();
        }
        return headers;
    }
}
