package io.github.orionlibs.core;

import io.github.orionlibs.core.event.EventPublisher;
import io.github.orionlibs.core.event.EventPublisher.EventPublisherFake;
import io.github.orionlibs.core.json.JSONService;
import io.github.orionlibs.core.json.JsonObjectMapper;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "io.github.orionlibs")
@Configuration
public class TestApplication extends SpringBootServletInitializer implements WebMvcConfigurer
{
    public static void main(String[] args)
    {
        SpringApplication.run(TestApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


    @Bean
    public JSONService jsonService(JsonObjectMapper objectMapper)
    {
        return new JSONService(objectMapper.getMapper());
    }


    @Bean
    public EventPublisher eventPublisherFake()
    {
        return new EventPublisherFake();
    }
}