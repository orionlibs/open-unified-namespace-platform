package io.github.orionlibs.document.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

@Schema(name = "DocumentsDTO", description = "Wrapper for a list of documents")
public record DocumentsDTO(List<DocumentDTO> documents) implements Serializable
{
}
