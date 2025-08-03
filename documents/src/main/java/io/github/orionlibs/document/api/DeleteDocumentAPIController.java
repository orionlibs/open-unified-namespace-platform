package io.github.orionlibs.document.api;

import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Tag(name = "Documents", description = "Document manager")
public class DeleteDocumentAPIController
{
    @Autowired
    private DocumentService documentService;


    @Operation(
                    summary = "Delete document by ID",
                    description = "Delete document by ID",
                    parameters = @io.swagger.v3.oas.annotations.Parameter(
                                    name = "documentID",
                                    description = "The ID of the document to delete",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int32")
                    ),
                    responses = {@ApiResponse(responseCode = "200", description = "Document deleted")}
    )
    @DeleteMapping(value = "/documents/{documentID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGER')")
    public ResponseEntity<?> deleteDocumentByID(@PathVariable(name = "documentID") Integer documentID)
    {
        documentService.delete(documentID);
        return ResponseEntity.ok(Map.of());
    }
}