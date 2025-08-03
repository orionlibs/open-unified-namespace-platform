package io.github.orionlibs.core.api;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricNumberOfAccessDeniedErrors extends Metric
{
    public MetricNumberOfAccessDeniedErrors(MeterRegistry meterRegistry)
    {
        super("api.access.denied.error.count", "Number of access denied errors", meterRegistry);
    }
}
