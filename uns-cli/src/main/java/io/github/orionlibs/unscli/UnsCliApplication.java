package io.github.orionlibs.unscli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Configuration
@CommandScan
public class UnsCliApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(UnsCliApplication.class, args);
    }


    @Bean
    public WebClient unsWebClient(CredentialsProvider credentials)
    {
        //point to your API host; you can also externalize this to application.properties
        return WebClient.builder()
                        .baseUrl("http://localhost:8080")
                        .filter(credentialFilter(credentials))
                        .build();
    }


    private ExchangeFilterFunction credentialFilter(CredentialsProvider creds)
    {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            ClientRequest authorizedRequest = ClientRequest.from(clientRequest)
                            .header("x-api-key", creds.getApiKey())
                            .header("x-api-secret", creds.getApiSecret())
                            .build();
            return Mono.just(authorizedRequest);
        });
    }
}
