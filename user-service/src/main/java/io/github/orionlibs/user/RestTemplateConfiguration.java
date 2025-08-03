package io.github.orionlibs.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration
{
    @Bean
    public RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(bearerTokenInterceptor());
        return restTemplate;
    }


    private ClientHttpRequestInterceptor bearerTokenInterceptor()
    {
        return (request, body, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication instanceof JwtAuthenticationToken jwtAuth)
            {
                String tokenValue = jwtAuth.getToken().getTokenValue();
                request.getHeaders().setBearerAuth(tokenValue);
            }
            return execution.execute(request, body);
        };
    }
}
