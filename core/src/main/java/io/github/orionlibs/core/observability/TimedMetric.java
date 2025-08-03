package io.github.orionlibs.core.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

public abstract class TimedMetric extends Metric
{
    protected Timer timer;


    public TimedMetric(String name, String description, MeterRegistry meterRegistry, String timerName, String timerDescription)
    {
        super(name, description, meterRegistry);
        this.timer = Timer.builder(timerName)
                        .description(timerDescription)
                        .register(meterRegistry);
    }


    public abstract void update(Runnable taskToCountExecutionDurationFor);
}
