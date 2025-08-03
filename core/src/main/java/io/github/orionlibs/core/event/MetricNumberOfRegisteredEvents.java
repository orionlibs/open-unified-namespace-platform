package io.github.orionlibs.core.event;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricNumberOfRegisteredEvents extends Metric
{
    public MetricNumberOfRegisteredEvents(MeterRegistry meterRegistry)
    {
        super("event.count", "Number of registered events", meterRegistry);
    }
}
