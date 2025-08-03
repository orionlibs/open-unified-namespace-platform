package io.github.orionlibs.core.event;

import java.util.Objects;

public interface Publishable
{
    default void publish(String eventName, Object payload)
    {
        Objects.requireNonNull(eventName, "eventName must not be null");
        String json = PublishableContext.json().toJson(payload);
        PublishableContext.publisher().publish(eventName, json);
    }
}
