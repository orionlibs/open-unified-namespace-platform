package io.github.orionlibs.core.api;

import io.github.orionlibs.core.Logger;
import io.github.orionlibs.core.data.DuplicateRecordException;
import io.github.orionlibs.core.data.ResourceNotFoundException;
import io.github.orionlibs.core.event.Publishable;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler implements Publishable
{
    @Value("${error.api.validation.message:Validation failed for one or more fields}")
    private String validationErrorMessage;
    @Value("${error.database.duplicate_record.message:Duplicate database record found: }")
    private String duplicateDatabaseRecordErrorMessage;
    @Value("${error.api.not_found.message:Resource not found: }")
    private String resourceNotFoundErrorMessage;
    @Value("${error.api.access_denied.message:Access denied}")
    private String accessDeniedErrorMessage;
    @Value("${error.api.generic_error.message:An unexpected error occurred}")
    private String genericErrorErrorMessage;
    @Autowired
    private MetricNumberOfAPIInputValidationErrors metricNumberOfAPIInputValidationErrors;
    @Autowired
    private MetricNumberOfDuplicateRecordErrors metricNumberOfDuplicateRecordErrors;
    @Autowired
    private MetricNumberOfResourceNotFoundErrors metricNumberOfResourceNotFoundErrors;
    @Autowired
    private MetricNumberOfAccessDeniedErrors metricNumberOfAccessDeniedErrors;
    @Autowired
    private MetricNumberOfUnknownErrors metricNumberOfUnknownErrors;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> onValidationError(MethodArgumentNotValidException ex)
    {
        List<APIField> fields = ex.getBindingResult()
                        .getFieldErrors().stream()
                        .map(fe -> new APIField(
                                        fe.getField(),
                                        fe.getDefaultMessage(),
                                        fe.getRejectedValue()))
                        .toList();
        APIError body = new APIError(
                        OffsetDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        validationErrorMessage,
                        fields);
        Logger.error("Invalid API input");
        metricNumberOfAPIInputValidationErrors.update();
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<APIError> onDuplicateRecordException(DuplicateRecordException ex)
    {
        Logger.error("Duplicate database record found: {}", ex.getMessage());
        metricNumberOfDuplicateRecordErrors.update();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIError(
                        OffsetDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        duplicateDatabaseRecordErrorMessage + ex.getMessage(),
                        null));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> onResourceNotFoundException(ResourceNotFoundException ex)
    {
        Logger.error("Resource not found: {}", ex.getMessage());
        metricNumberOfResourceNotFoundErrors.update();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIError(
                        OffsetDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        resourceNotFoundErrorMessage + ex.getMessage(),
                        null));
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<APIError> handleForbiddenExceptions(Exception ex)
    {
        APIError apiError = new APIError(
                        OffsetDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        accessDeniedErrorMessage,
                        null);
        Logger.error("Access denied: {}", ex.getMessage());
        metricNumberOfAccessDeniedErrors.update();
        return ResponseEntity.status(apiError.status()).body(apiError);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleAllCheckedExceptions(Exception ex)
    {
        APIError apiError = new APIError(
                        OffsetDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        genericErrorErrorMessage,
                        null);
        Logger.error("Uncaught checked exception: {}", ex.getMessage());
        publish(EventUnknownError.EVENT_NAME, EventUnknownError.builder()
                        .error(ex.getMessage())
                        .build());
        metricNumberOfUnknownErrors.update();
        return ResponseEntity.status(apiError.status()).body(apiError);
    }
}
