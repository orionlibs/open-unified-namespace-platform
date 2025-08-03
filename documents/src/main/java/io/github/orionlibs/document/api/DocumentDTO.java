package io.github.orionlibs.document.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.orionlibs.document.model.DocumentType;
import java.io.Serializable;
import java.time.LocalDateTime;

public record DocumentDTO(@JsonProperty("document_url") String documentURL,
                          @JsonProperty("document_type") DocumentType.Type type,
                          String title,
                          String description,
                          @JsonProperty("created_at") LocalDateTime createdAt,
                          @JsonProperty("updated_at") LocalDateTime updatedAt) implements Serializable
{
}
