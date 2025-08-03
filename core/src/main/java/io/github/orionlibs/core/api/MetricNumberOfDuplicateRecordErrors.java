package io.github.orionlibs.core.api;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricNumberOfDuplicateRecordErrors extends Metric
{
    public MetricNumberOfDuplicateRecordErrors(MeterRegistry meterRegistry)
    {
        super("data.duplicate.record.error.count", "Number of duplicate record errors", meterRegistry);
    }
}
