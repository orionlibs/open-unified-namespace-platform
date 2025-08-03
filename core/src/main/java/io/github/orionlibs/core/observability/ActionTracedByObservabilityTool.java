package io.github.orionlibs.core.observability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.stereotype.Service;

@Service
public class ActionTracedByObservabilityTool
{
    private final ObservationRegistry observationRegistry;


    public ActionTracedByObservabilityTool(ObservationRegistry observationRegistry)
    {
        this.observationRegistry = observationRegistry;
    }


    public void doSomethingObserved()
    {
        Observation.createNotStarted("my.custom.observation", observationRegistry)
                        .lowCardinalityKeyValue("step", "one")
                        .observe(() -> {
                            // your code to trace/observe
                            simulateWork();
                        });
    }


    private void simulateWork()
    {
        try
        {
            Thread.sleep(200);
        }
        catch(InterruptedException ignored)
        {
        }
    }
}
