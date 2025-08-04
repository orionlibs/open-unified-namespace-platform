package io.github.orionlibs.user.authentication.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.api.APIError;
import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.user.ControllerUtils;
import io.github.orionlibs.user.registration.UserRegistrationService;
import io.github.orionlibs.core.user.model.UserDAO;
import io.github.orionlibs.user.registration.api.UserRegistrationRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired UserDAO dao;
    @Autowired APITestUtils apiUtils;
    String basePath;
    @Autowired UserRegistrationService userRegistrationService;


    @BeforeEach
    public void setUp()
    {
        basePath = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/users/login";
        dao.deleteAll();
        userRegistrationService.registerUser(UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("bunkzh3Z!")
                        .authority("USER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build());
    }


    @Test
    void loginUser()
    {
        RestAssured.baseURI = basePath;
        LoginRequest request = LoginRequest.builder()
                        .username("me@email.com")
                        .password("bunkzh3Z!")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    void loginUser_invalidUsername()
    {
        RestAssured.baseURI = basePath;
        LoginRequest request = LoginRequest.builder()
                        .username("")
                        .password("bunkzh3Z!")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(404);
        APIError body = response.as(APIError.class);
        assertThat(body.message()).isEqualTo("Resource not found: User not found");
    }


    @Test
    void loginUser_invalidPassword()
    {
        RestAssured.baseURI = basePath;
        LoginRequest request = LoginRequest.builder()
                        .username("me@email.com")
                        .password("4528")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.message()).isEqualTo("Validation failed for one or more fields");
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("Password does not meet security requirements");
    }


    @Test
    void loginUser_invalidUsernamePassword()
    {
        RestAssured.baseURI = basePath;
        LoginRequest request = LoginRequest.builder()
                        .username("me")
                        .password("4528")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        Set<String> errorMessages = body.fieldErrors().stream().map(e -> e.message()).collect(Collectors.toSet());
        assertThat(body.message()).isEqualTo("Validation failed for one or more fields");
        assertThat(errorMessages).isEqualTo(Set.of("Password does not meet security requirements",
                        "Invalid email address format"));
    }
}
