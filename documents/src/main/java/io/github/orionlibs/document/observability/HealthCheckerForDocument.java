package io.github.orionlibs.document.observability;

import io.github.orionlibs.core.observability.HealthChecker;
import io.github.orionlibs.document.model.DocumentDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
//GET /actuator/health
public class HealthCheckerForDocument implements HealthChecker
{
    @Autowired
    private DocumentDAORepository dao;


    @Override
    public Health health()
    {
        boolean serverHealthy = checkSomeCustomLogic();
        if(serverHealthy)
        {
            return Health.up().withDetail("custom-check", "Everything OK").build();
        }
        else
        {
            return Health.down().withDetail("custom-check", "Something went wrong").build();
        }
    }


    private boolean checkSomeCustomLogic()
    {
        try
        {
            Integer result = dao.testConnection();
            return result != null && result == 1;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
