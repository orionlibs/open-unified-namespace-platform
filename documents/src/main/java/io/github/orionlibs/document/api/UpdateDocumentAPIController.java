package io.github.orionlibs.document.api;

import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.converter.DocumentEntityToDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Validated
@Tag(name = "Documents", description = "Document manager")
public class UpdateDocumentAPIController
{
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentEntityToDTOConverter documentEntityToDTOConverter;


    @Operation(
                    summary = "Update document",
                    description = "Update document",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(
                                                    schema = @Schema(implementation = UpdateDocumentRequest.class)
                                    )
                    ),
                    responses = {@ApiResponse(responseCode = "201", description = "Document updated",
                                    content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = DocumentsDTO.class)
                                    )),
                                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                                    @ApiResponse(responseCode = "404", description = "Document not found")}
    )
    @PutMapping(value = "/documents/{documentID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGER')")
    public ResponseEntity<?> updateDocument(@PathVariable(name = "documentID") Integer documentID, @Valid @RequestBody UpdateDocumentRequest documentToUpdate)
    {
        boolean isDocumentFound = documentService.update(documentID, documentToUpdate);
        if(isDocumentFound)
        {
            return ResponseEntity.ok(Map.of());
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}