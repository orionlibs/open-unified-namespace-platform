package io.github.orionlibs.document.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.model.DocumentModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DeleteDocumentAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired DocumentService documentService;
    @Autowired TestUtils utils;
    @Autowired APITestUtils apiUtils;
    HttpHeaders headers;


    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp()
    {
        documentService.deleteAll();
        headers = new HttpHeaders();
        RestAssured.baseURI = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/documents";
    }


    @Test
    void getDocumentByID_noResults()
    {
        RestAssured.baseURI += "/100";
        Response response = apiUtils.makeDeleteAPICall(headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    void getDocumentByID_noResults_anonymous()
    {
        RestAssured.baseURI += "/100";
        Response response = apiUtils.makeDeleteAPICall(headers);
        assertThat(response.statusCode()).isEqualTo(403);
    }


    @Test
    void getDocumentByID()
    {
        DocumentModel doc1 = utils.saveDocument("https://company.com/1.pdf");
        RestAssured.baseURI += "/" + doc1.getId();
        Response response = apiUtils.makeDeleteAPICall(headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
        response = apiUtils.makeGetAPICall(headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(404);
    }


    @Test
    void getDocumentByID_anonymous()
    {
        DocumentModel doc1 = utils.saveDocument("https://company.com/1.pdf");
        RestAssured.baseURI += "/" + doc1.getId();
        Response response = apiUtils.makeDeleteAPICall(headers);
        assertThat(response.statusCode()).isEqualTo(403);
    }
}
