package io.github.orionlibs.core.event;

import io.github.orionlibs.core.Logger;

public interface EventPublisher
{
    void publish(String topic, String message);


    class EventPublisherFake implements EventPublisher
    {
        @Override
        public void publish(String topic, String message)
        {
            //fake implementation
            Logger.info("publishing event to topic {}", topic);
        }
    }
}
