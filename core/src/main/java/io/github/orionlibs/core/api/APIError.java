package io.github.orionlibs.core.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record APIError(
                OffsetDateTime timestamp,
                int status,
                String message,
                @JsonProperty("field_errors") List<APIField> fieldErrors
)
{
}
