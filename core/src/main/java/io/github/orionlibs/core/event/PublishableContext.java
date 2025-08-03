package io.github.orionlibs.core.event;

import io.github.orionlibs.core.json.JSONService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublishableContext
{
    private static JSONService jsonService;
    private static EventPublisher eventPublisher;


    @Autowired
    public void setJsonService(JSONService jsonService)
    {
        PublishableContext.jsonService = jsonService;
    }


    @Autowired
    public void setEventPublisher(EventPublisher publisher)
    {
        PublishableContext.eventPublisher = publisher;
    }


    public static JSONService json()
    {
        return jsonService;
    }


    public static EventPublisher publisher()
    {
        return eventPublisher;
    }
}
