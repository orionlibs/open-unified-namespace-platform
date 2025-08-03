package io.github.orionlibs.document.api;

import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.core.tests.UserForTest;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.model.DocumentModel;
import io.github.orionlibs.document.model.DocumentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TestUtils
{
    @Autowired APITestUtils apiUtils;
    @Autowired DocumentService documentService;
    String basePath;

    static
    {
        RestAssured.useRelaxedHTTPSValidation();
    }

    DocumentModel saveDocument(String documentURL)
    {
        DocumentModel doc = new DocumentModel(documentURL, DocumentType.Type.DOCUMENTATION, "document title 1", "document description 1");
        return documentService.save(doc);
    }


    void saveUser(int port, String endpointURL, String username, String password, String authority)
    {
        basePath = "http://localhost:" + port + endpointURL;
        UserForTest request = UserForTest.builder()
                        .username(username)
                        .password(password)
                        .authority(authority)
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
    }


    String loginUserAndGetJWT(int port, String endpointURL, String username, String password)
    {
        basePath = "http://localhost:" + port + endpointURL;
        UserForTest request = UserForTest.builder()
                        .username(username)
                        .password(password)
                        .build();
        Response response = apiUtils.makePostAPICall(request, null);
        Map<?, ?> body = response.as(Map.class);
        return (String)body.get("token");
    }
}
