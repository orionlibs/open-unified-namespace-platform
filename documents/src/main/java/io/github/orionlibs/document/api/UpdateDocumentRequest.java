package io.github.orionlibs.document.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.orionlibs.document.model.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used as request bean in the API holding the data of a document to be updated
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateDocumentRequest implements Serializable
{
    @NotBlank(message = "document_url must not be blank")
    @JsonProperty("document_url")
    private String documentURL;
    @NotNull(message = "type must not be blank")
    @JsonProperty("type")
    private DocumentType.Type type;
    @NotBlank(message = "title must not be blank")
    private String title;
    private String description;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
