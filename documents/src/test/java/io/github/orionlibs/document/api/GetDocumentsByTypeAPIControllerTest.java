package io.github.orionlibs.document.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.model.DocumentModel;
import io.github.orionlibs.document.model.DocumentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GetDocumentsByTypeAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired DocumentService documentService;
    @Autowired TestUtils utils;
    @Autowired APITestUtils apiUtils;
    String jwtToken;
    HttpHeaders headers;


    @BeforeEach
    public void setUp()
    {
        documentService.deleteAll();
        headers = new HttpHeaders();
        RestAssured.baseURI = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/documents/types";
    }


    @Test
    void getDocumentsByType_noResults()
    {
        RestAssured.baseURI += "/" + DocumentType.Type.DOCUMENTATION.name();
        Response response = apiUtils.makeGetAPICall(null, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
        DocumentsDTO body = response.as(DocumentsDTO.class);
        assertThat(body.documents().isEmpty()).isTrue();
    }


    @Test
    void getDocumentsByType_noResults_anonymous()
    {
        RestAssured.baseURI += "/" + DocumentType.Type.DOCUMENTATION.name();
        Response response = apiUtils.makeGetAPICall(null);
        assertThat(response.statusCode()).isEqualTo(403);
    }


    @Test
    void getDocumentsByType()
    {
        DocumentModel doc1 = utils.saveDocument("https://company.com/1.pdf");
        DocumentModel doc2 = utils.saveDocument("https://company.com/2.pdf");
        RestAssured.baseURI += "/" + DocumentType.Type.DOCUMENTATION.name();
        Response response = apiUtils.makeGetAPICall(null, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
        DocumentsDTO body = response.as(DocumentsDTO.class);
        assertThat(body.documents().size()).isEqualTo(2);
        assertThat(body.documents().get(0).documentURL()).isEqualTo("https://company.com/1.pdf");
        assertThat(body.documents().get(1).documentURL()).isEqualTo("https://company.com/2.pdf");
    }


    @Test
    void getDocumentsByType_anonymous()
    {
        DocumentModel doc1 = utils.saveDocument("https://company.com/1.pdf");
        DocumentModel doc2 = utils.saveDocument("https://company.com/2.pdf");
        RestAssured.baseURI += "/" + DocumentType.Type.DOCUMENTATION.name();
        Response response = apiUtils.makeGetAPICall(null);
        assertThat(response.statusCode()).isEqualTo(403);
    }
}
