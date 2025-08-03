package io.github.orionlibs.document.api;

import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.converter.DocumentEntityToDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
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
public class GetDocumentByIDAPIController
{
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentEntityToDTOConverter documentEntityToDTOConverter;


    @Operation(
                    summary = "Get document by ID",
                    description = "Get document by ID",
                    parameters = @io.swagger.v3.oas.annotations.Parameter(
                                    name = "documentID",
                                    description = "The ID of the document to retrieve",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int32")
                    ),
                    responses = {@ApiResponse(responseCode = "200", description = "Document found",
                                    content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = DocumentDTO.class)
                                    )),
                                    @ApiResponse(responseCode = "404", description = "Document not found")}
    )
    @GetMapping(value = "/documents/{documentID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGER')")
    public ResponseEntity<DocumentDTO> getDocumentByID(@PathVariable(name = "documentID") Integer documentID)
    {
        return documentService.getByID(documentID)
                        .map(documentEntityToDTOConverter::convert)
                        .filter(Objects::nonNull)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
}