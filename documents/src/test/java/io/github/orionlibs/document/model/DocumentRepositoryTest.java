package io.github.orionlibs.document.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentRepositoryTest
{
    @Autowired DocumentDAORepository dao;


    @BeforeEach
    void setup()
    {
        dao.deleteAll();
    }


    @Test
    void saveDocumentAndReturnDocument()
    {
        // given
        DocumentModel doc1 = saveDocument("https://company.com/1.pdf");
        // when
        DocumentModel savedDoc = dao.save(doc1);
        // then
        assertThat(savedDoc).isNotNull();
        assertThat(savedDoc.getId()).isGreaterThan(0);
        assertThat(savedDoc.getDocumentURL()).isEqualTo("https://company.com/1.pdf");
        assertThat(savedDoc.getCreatedAt().toString()).isNotBlank();
        assertThat(savedDoc.getUpdatedAt().toString()).isNotBlank();
    }


    @Test
    void saveDocumentsAndReturnByType()
    {
        // given
        DocumentModel doc1 = saveDocument("https://company.com/1.pdf");
        DocumentModel doc2 = saveDocument("https://company.com/2.pdf");
        // when
        DocumentModel savedDoc1 = dao.save(doc1);
        DocumentModel savedDoc2 = dao.save(doc2);
        List<DocumentModel> docs = dao.findAllByType(DocumentType.Type.DOCUMENTATION);
        // then
        assertThat(docs).isNotNull();
        assertThat(docs.size()).isEqualTo(2);
        assertThat(docs.get(0).getId()).isGreaterThan(0);
        assertThat(docs.get(1).getId()).isGreaterThan(0);
        assertThat(docs.get(0).getDocumentURL()).isEqualTo("https://company.com/1.pdf");
        assertThat(docs.get(1).getDocumentURL()).isEqualTo("https://company.com/2.pdf");
        assertThat(docs.get(0).getCreatedAt().toString()).isNotBlank();
        assertThat(docs.get(0).getUpdatedAt().toString()).isNotBlank();
        assertThat(docs.get(1).getCreatedAt().toString()).isNotBlank();
        assertThat(docs.get(1).getUpdatedAt().toString()).isNotBlank();
    }


    private DocumentModel saveDocument(String documentURL)
    {
        DocumentModel doc = new DocumentModel(documentURL, DocumentType.Type.DOCUMENTATION, "doc title 1", "doc description 1");
        return dao.save(doc);
    }
}
