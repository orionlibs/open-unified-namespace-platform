package io.github.orionlibs.document.event;

import io.github.orionlibs.core.event.Event;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
//@AllArgsConstructor
@Builder
@Getter
@Setter
@Event
public class EventDocumentDeletedAll implements Serializable
{
    public static final String EVENT_NAME = "document-deleted-all";
}
