package io.github.orionlibs.user.registration.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.api.APIError;
import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.user.ControllerUtils;
import io.github.orionlibs.core.user.UserAuthority;
import io.github.orionlibs.core.user.model.UserDAO;
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
class SaveUserAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired APITestUtils apiUtils;
    String basePath;
    @Autowired UserDAO dao;


    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp()
    {
        dao.deleteAll();
        basePath = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/users";
    }


    @Test
    void saveUser()
    {
        RestAssured.baseURI = basePath;
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("bunkzh3Z!")
                        .authority(UserAuthority.ADMINISTRATOR.name() + ",CUSTOMER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(201);
    }


    @Test
    void saveUser_invalidUsername()
    {
        RestAssured.baseURI = basePath;
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me")
                        .password("bunkzh3Z!")
                        .authority(UserAuthority.ADMINISTRATOR.name() + ",CUSTOMER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("Invalid email address format");
    }


    @Test
    void saveUser_duplicateUsername()
    {
        RestAssured.baseURI = basePath;
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("bunkzh3Z!")
                        .authority(UserAuthority.ADMINISTRATOR.name() + ",CUSTOMER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        apiUtils.makePostAPICall(request, null);
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(409);
        APIError body = response.as(APIError.class);
        assertThat(body.message()).isEqualTo("Duplicate database record found: This user already exists");
    }


    @Test
    void saveUser_invalidPassword()
    {
        RestAssured.baseURI = basePath;
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("4528")
                        .authority(UserAuthority.ADMINISTRATOR.name() + ",CUSTOMER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("Password does not meet security requirements");
    }


    @Test
    void saveUser_invalidAuthority()
    {
        RestAssured.baseURI = basePath;
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("bunkzh3Z!")
                        .authority("")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("Authority must not be blank");
    }


    @Test
    void saveUser_invalidUsernamePasswordAuthority()
    {
        RestAssured.baseURI = basePath;
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me")
                        .password("4528")
                        .authority("")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        Set<String> errorMessages = body.fieldErrors().stream().map(e -> e.message()).collect(Collectors.toSet());
        assertThat(errorMessages).isEqualTo(Set.of("Authority must not be blank",
                        "Password does not meet security requirements",
                        "Invalid email address format"));
    }
}
