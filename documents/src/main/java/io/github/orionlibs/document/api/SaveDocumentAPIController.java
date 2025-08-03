package io.github.orionlibs.document.api;

import static org.springframework.http.ResponseEntity.created;

import io.github.orionlibs.document.ControllerUtils;
import io.github.orionlibs.document.DocumentService;
import io.github.orionlibs.document.converter.DocumentEntityToDTOConverter;
import io.github.orionlibs.document.model.DocumentModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerUtils.baseAPIPath)
@Validated
@Tag(name = "Documents", description = "Document manager")
public class SaveDocumentAPIController
{
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentEntityToDTOConverter documentEntityToDTOConverter;
    /*@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;*/


    @Operation(
                    summary = "Save document",
                    description = "Save document",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(
                                                    schema = @Schema(implementation = SaveDocumentRequest.class)
                                    )
                    ),
                    responses = {@ApiResponse(responseCode = "201", description = "Document saved",
                                    content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = DocumentsDTO.class)
                                    )),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")}
    )
    @PostMapping(value = "/documents", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('DOCUMENT_MANAGER')")
    public ResponseEntity<?> saveDocument(@Valid @RequestBody SaveDocumentRequest documentToSave)
    {
        DocumentModel newDocument = documentService.save(documentToSave);
        /*kafkaTemplate.send(DocumentSavedEvent.EVENT_NAME, jsonService.toJson(DocumentSavedEvent.builder()
                        .documentID(newDocument.getId())
                        .documentLocation(newDocumentURL)
                        .build()));*/
        return created(URI.create(newDocument.getDocumentURL())).body(Map.of());
    }
}