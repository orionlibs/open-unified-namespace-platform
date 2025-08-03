package io.github.orionlibs.document.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.model.DocumentModel;
import io.github.orionlibs.document.model.DocumentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UpdateDocumentAPIControllerTest
{
    @LocalServerPort int port;
    @Autowired DocumentService documentService;
    @Autowired TestUtils utils;
    @Autowired APITestUtils apiUtils;
    String jwtToken;
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
    void updateDocument_notFound()
    {
        RestAssured.baseURI += "/100";
        SaveDocumentRequest doc = updateDocumentRequest("https://company.com/1.pdf");
        Response response = apiUtils.makePutAPICall(doc, headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(404);
    }


    @Test
    void updateDocument_notFound_anonymous()
    {
        RestAssured.baseURI += "/100";
        SaveDocumentRequest doc = updateDocumentRequest("https://company.com/1.pdf");
        Response response = apiUtils.makePutAPICall(doc, headers);
        assertThat(response.statusCode()).isEqualTo(403);
    }


    @Test
    void updateDocument()
    {
        DocumentModel doc = utils.saveDocument("https://company.com/1.pdf");
        RestAssured.baseURI += "/" + doc.getId();
        SaveDocumentRequest docToUpdate = updateDocumentRequest("https://company.com/1.pdf");
        docToUpdate.setDocumentURL("https://company.com/2.pdf");
        docToUpdate.setType(DocumentType.Type.OTHER);
        docToUpdate.setTitle("new title");
        docToUpdate.setDescription("new description");
        Response response = apiUtils.makePutAPICall(docToUpdate, headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
        response = apiUtils.makeGetAPICall(null, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
        DocumentDTO body = response.as(DocumentDTO.class);
        assertThat(body.documentURL()).isEqualTo("https://company.com/2.pdf");
        assertThat(body.type()).isEqualTo(DocumentType.Type.OTHER);
        assertThat(body.title()).isEqualTo("new title");
        assertThat(body.description()).isEqualTo("new description");
    }


    @Test
    void updateDocument_anonymous()
    {
        DocumentModel doc = utils.saveDocument("https://company.com/1.pdf");
        RestAssured.baseURI += "/" + doc.getId();
        SaveDocumentRequest docToUpdate = updateDocumentRequest("https://company.com/1.pdf");
        docToUpdate.setDocumentURL("https://company.com/2.pdf");
        docToUpdate.setType(DocumentType.Type.OTHER);
        docToUpdate.setTitle("new title");
        docToUpdate.setDescription("new description");
        Response response = apiUtils.makePutAPICall(docToUpdate, headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(200);
        response = apiUtils.makeGetAPICall(null);
        assertThat(response.statusCode()).isEqualTo(403);
    }


    private SaveDocumentRequest updateDocumentRequest(String docURL)
    {
        return new SaveDocumentRequest(docURL, DocumentType.Type.DOCUMENTATION, "title", "description", LocalDateTime.now(), LocalDateTime.now());
    }
}
