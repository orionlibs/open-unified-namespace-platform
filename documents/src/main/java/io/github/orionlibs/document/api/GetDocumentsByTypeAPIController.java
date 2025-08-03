package io.github.orionlibs.document.api;

import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.converter.DocumentEntityToDTOConverter;
import io.github.orionlibs.document.model.DocumentModel;
import io.github.orionlibs.document.model.DocumentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Tag(name = "Documents", description = "Document manager")
public class GetDocumentsByTypeAPIController
{
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentEntityToDTOConverter documentEntityToDTOConverter;


    @Operation(
                    summary = "Get documents by document type",
                    description = "Get documents by document type",
                    parameters = @io.swagger.v3.oas.annotations.Parameter(
                                    required = true,
                                    in = ParameterIn.QUERY,
                                    content = @Content(schema = @Schema(implementation = DocumentType.Type.class))
                    ),
                    responses = {@ApiResponse(responseCode = "200", description = "Cases found",
                                    content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = DocumentsDTO.class)
                                    ))}
    )
    @GetMapping(value = "/documents/types/{documentType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGER')")
    public ResponseEntity<DocumentsDTO> getDocumentsByType(@PathVariable(name = "documentType") DocumentType.Type documentType)
    {
        List<DocumentModel> documentsFound = documentService.getByType(documentType);
        List<DocumentDTO> documentsToReturn = documentsFound.stream()
                        .map(doc -> documentEntityToDTOConverter.convert(doc))
                        .toList();
        return ResponseEntity.ok(new DocumentsDTO(documentsToReturn));
    }
}