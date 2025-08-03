package io.github.orionlibs.core.observability;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
//GET /actuator/health
public interface HealthChecker extends HealthIndicator
{
    Health health();
}
