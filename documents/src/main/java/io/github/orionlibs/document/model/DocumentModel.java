package io.github.orionlibs.document.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "documents", schema = "uns", indexes = {
                @Index(name = "idx_uns_documents", columnList = "id")
})
public class DocumentModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id_of_uploader", updatable = false)
    private UUID userIDOfUploader;
    @Column(name = "user_id_of_owner", updatable = false)
    private UUID userIDOfOwner;
    @Column(name = "document_URL", length = 700, nullable = false)
    private String documentURL;
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20, nullable = false)
    private DocumentType.Type type;
    @Column(name = "title", length = 700, nullable = false)
    private String title;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public DocumentModel()
    {
    }


    public DocumentModel(String documentURL, DocumentType.Type type, String title, String description)
    {
        this.documentURL = documentURL;
        this.type = type;
        this.title = title;
        this.description = description;
    }


    public Integer getId()
    {
        return id;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


    public String getDocumentURL()
    {
        return documentURL;
    }


    public void setDocumentURL(String documentURL)
    {
        this.documentURL = documentURL;
    }


    public DocumentType.Type getType()
    {
        return type;
    }


    public void setType(DocumentType.Type type)
    {
        this.type = type;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
