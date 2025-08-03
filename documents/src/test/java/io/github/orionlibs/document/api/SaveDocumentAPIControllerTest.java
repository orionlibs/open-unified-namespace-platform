package io.github.orionlibs.document.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.api.APIError;
import io.github.orionlibs.core.tests.APITestUtils;
import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
@ActiveProfiles("test")
class SaveDocumentAPIControllerTest
{
    @LocalServerPort int port;
    /*@MockitoBean  ProducerFactory<String, String> producerFactory;
    @MockitoBean Producer<String, String> producer;*/
    @Autowired DocumentService documentService;
    @Autowired TestUtils utils;
    @Autowired APITestUtils apiUtils;
    String basePath;
    String jwtToken;
    HttpHeaders headers;


    @BeforeEach
    public void setUp()
    {
        documentService.deleteAll();
        headers = new HttpHeaders();
        basePath = "http://localhost:" + port + ControllerUtils.baseAPIPath + "/documents";
        /*System.setProperty(
                        "java.security.auth.login.config",
                        new File("src/test/resources/jaas.conf").getAbsolutePath()
        );
        producer = mock(org.apache.kafka.clients.producer.Producer.class);
        OngoingStubbing<Producer<String, String>> stub = when(producerFactory.createProducer());
        stub.thenReturn(producer);
        when(producerFactory.createProducer()).thenReturn(producer);
        when(producer.send(any(ProducerRecord.class), any(Callback.class)))
                        .thenAnswer(invocation -> {
                            ProducerRecord<String, String> record = invocation.getArgument(0);
                            Callback cb = invocation.getArgument(1);
                            RecordMetadata meta = new RecordMetadata(
                                            new TopicPartition(record.topic(), record.partition() != null ? record.partition() : 0),
                                            0L,                                            0,
                                            System.currentTimeMillis(),
                                            record.key() != null ? record.key().length() : 0,
                                            record.value() != null ? record.value().length() : 0
                            );
                            cb.onCompletion(meta, null);
                            return CompletableFuture.completedFuture(meta);
                        });*/
    }


    @Test
    void saveDocument()
    {
        RestAssured.baseURI = basePath;
        SaveDocumentRequest docToSave = saveDocumentRequest("https://company.com/1.pdf");
        Response response = apiUtils.makePostAPICall(docToSave, headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.header("Location")).isEqualTo("https://company.com/1.pdf");
    }


    @Test
    void saveDocument_anonymous()
    {
        RestAssured.baseURI = basePath;
        SaveDocumentRequest docToSave = saveDocumentRequest("https://company.com/1.pdf");
        Response response = apiUtils.makePostAPICall(docToSave, headers);
        assertThat(response.statusCode()).isEqualTo(403);
    }


    @Test
    void saveDocument_invalidDocumentType()
    {
        RestAssured.baseURI = basePath;
        SaveDocumentRequest docToSave = saveDocumentRequestWithoutType("https://company.com/1.pdf");
        Response response = apiUtils.makePostAPICall(docToSave, headers, "Jimmy", "DOCUMENT_MANAGER");
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("type must not be blank");
    }


    @Test
    void saveDocument_invalidDocumentType_anonymous()
    {
        RestAssured.baseURI = basePath;
        SaveDocumentRequest docToSave = saveDocumentRequestWithoutType("https://company.com/1.pdf");
        Response response = apiUtils.makePostAPICall(docToSave, headers);
        assertThat(response.statusCode()).isEqualTo(400);
        APIError body = response.as(APIError.class);
        assertThat(body.fieldErrors().get(0).message()).isEqualTo("type must not be blank");
    }


    private SaveDocumentRequest saveDocumentRequest(String docURL)
    {
        return new SaveDocumentRequest(docURL, DocumentType.Type.DOCUMENTATION, "title", "description", LocalDateTime.now(), LocalDateTime.now());
    }


    private SaveDocumentRequest saveDocumentRequestWithoutType(String docURL)
    {
        return new SaveDocumentRequest(docURL, null, "title", "description", LocalDateTime.now(), LocalDateTime.now());
    }
}
