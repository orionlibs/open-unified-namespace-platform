package io.github.orionlibs.core.api;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricNumberOfAPICalls extends Metric
{
    public MetricNumberOfAPICalls(MeterRegistry meterRegistry)
    {
        super("api.call.count", "Number of API calls", meterRegistry);
    }
}
