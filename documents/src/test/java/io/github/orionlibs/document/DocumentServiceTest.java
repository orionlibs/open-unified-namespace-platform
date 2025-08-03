package io.github.orionlibs.document;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.document.model.DocumentModel;
import io.github.orionlibs.document.model.DocumentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentServiceTest
{
    @Autowired DocumentService documentService;


    @BeforeEach
    void setup()
    {
        documentService.deleteAll();
    }


    @Test
    void saveDocumentsAndReturnByType()
    {
        // given
        DocumentModel doc1 = saveDocument("https://company.com/1.pdf", DocumentType.Type.DOCUMENTATION);
        DocumentModel doc2 = saveDocument("https://company.com/2.pdf", DocumentType.Type.DOCUMENTATION);
        DocumentModel doc3 = saveDocument("https://company.com/3.pdf", DocumentType.Type.OTHER);
        // when
        List<DocumentModel> docs1 = documentService.getByType(DocumentType.Type.DOCUMENTATION);
        List<DocumentModel> docs2 = documentService.getByType(DocumentType.Type.OTHER);
        // then
        assertThat(docs1).isNotNull();
        assertThat(docs2).isNotNull();
        assertThat(docs1.size()).isEqualTo(2);
        assertThat(docs2.size()).isEqualTo(1);
        assertThat(docs1.get(0).getId()).isGreaterThan(0);
        assertThat(docs1.get(1).getId()).isGreaterThan(0);
        assertThat(docs2.get(0).getId()).isGreaterThan(0);
        assertThat(docs1.get(0).getDocumentURL()).isEqualTo("https://company.com/1.pdf");
        assertThat(docs1.get(1).getDocumentURL()).isEqualTo("https://company.com/2.pdf");
        assertThat(docs2.get(0).getDocumentURL()).isEqualTo("https://company.com/3.pdf");
        assertThat(docs1.get(0).getType()).isEqualTo(DocumentType.Type.DOCUMENTATION);
        assertThat(docs1.get(1).getType()).isEqualTo(DocumentType.Type.DOCUMENTATION);
        assertThat(docs2.get(0).getType()).isEqualTo(DocumentType.Type.OTHER);
        assertThat(docs1.get(0).getTitle()).isEqualTo("doc title 1");
        assertThat(docs1.get(1).getTitle()).isEqualTo("doc title 1");
        assertThat(docs2.get(0).getTitle()).isEqualTo("doc title 1");
        assertThat(docs1.get(0).getDescription()).isEqualTo("doc description 1");
        assertThat(docs1.get(1).getDescription()).isEqualTo("doc description 1");
        assertThat(docs2.get(0).getDescription()).isEqualTo("doc description 1");
        assertThat(docs1.get(0).getCreatedAt().toString()).isNotBlank();
        assertThat(docs1.get(1).getCreatedAt().toString()).isNotBlank();
        assertThat(docs2.get(0).getCreatedAt().toString()).isNotBlank();
        assertThat(docs1.get(0).getUpdatedAt().toString()).isNotBlank();
        assertThat(docs1.get(1).getUpdatedAt().toString()).isNotBlank();
        assertThat(docs2.get(0).getUpdatedAt().toString()).isNotBlank();
    }


    private DocumentModel saveDocument(String documentURL, DocumentType.Type type)
    {
        DocumentModel doc = new DocumentModel(documentURL, type, "doc title 1", "doc description 1");
        return documentService.save(doc);
    }
}
