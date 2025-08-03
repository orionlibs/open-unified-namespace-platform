package io.github.orionlibs.document.event;

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
public class EventDocumentSaved implements Serializable
{
    public static final String EVENT_NAME = "document-saved";
    private Integer documentID;
    private String documentLocation;
}
