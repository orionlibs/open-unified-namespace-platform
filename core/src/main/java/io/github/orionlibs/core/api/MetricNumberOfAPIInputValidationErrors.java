package io.github.orionlibs.core.api;

import io.github.orionlibs.core.observability.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
//GET /actuator/metrics/api.input.validation.error.count
public class MetricNumberOfAPIInputValidationErrors extends Metric
{
    public MetricNumberOfAPIInputValidationErrors(MeterRegistry meterRegistry)
    {
        super("api.input.validation.error.count", "Number of API input validation errors", meterRegistry);
    }
}
