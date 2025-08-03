package io.github.orionlibs.core.api;

import io.github.orionlibs.core.event.Event;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Event
public class EventUnknownError implements Serializable
{
    public static final String EVENT_NAME = "unknown-error";
    private String error;
}
