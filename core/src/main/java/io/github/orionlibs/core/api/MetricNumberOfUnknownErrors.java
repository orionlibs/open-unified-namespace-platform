package io.github.orionlibs.core.api;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricNumberOfUnknownErrors extends Metric
{
    public MetricNumberOfUnknownErrors(MeterRegistry meterRegistry)
    {
        super("unknown.error.count", "Number of unknown errors", meterRegistry);
    }
}
