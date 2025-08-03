package io.github.orionlibs.core.api;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricNumberOfResourceNotFoundErrors extends Metric
{
    public MetricNumberOfResourceNotFoundErrors(MeterRegistry meterRegistry)
    {
        super("data.resource.not.found.error.count", "Number of resource-not-found errors", meterRegistry);
    }
}
