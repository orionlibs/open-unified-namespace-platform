package io.github.orionlibs.core.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Metric
{
    protected String name;
    protected String description;
    protected Counter counter;
    private MeterRegistry meterRegistry;
    private final ConcurrentMap<String, Counter> counterCache = new ConcurrentHashMap<>();


    public Metric(String name, String description)
    {
        this.name = name;
        this.description = description;
    }


    public Metric(String name, String description, MeterRegistry meterRegistry)
    {
        this.meterRegistry = meterRegistry;
        this.name = name;
        this.description = description;
    }


    public void update()
    {
        if(counter == null)
        {
            this.counter = Counter.builder(name)
                            .description(description)
                            .register(meterRegistry);
        }
        counter.increment();
    }


    public void update(String tagKey, String tagValue)
    {
        String cacheKey = name + "|" + tagKey + "|" + tagValue;
        Counter counter = counterCache.computeIfAbsent(cacheKey, key ->
                        Counter.builder(name)
                                        .description(description)
                                        .tag(tagKey, tagValue)
                                        .register(meterRegistry)
        );
        counter.increment();
    }


    public void update(int count)
    {
        if(counter == null)
        {
            this.counter = Counter.builder(name)
                            .description(description)
                            .register(meterRegistry);
        }
        counter.increment((double)count);
    }
}
