package io.github.orionlibs.document;

import io.github.orionlibs.core.api.ApiKeyAuthFilter;
import io.github.orionlibs.core.api.ApiKeyAuthenticationProvider;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration
{
    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String base64Secret)
    {
        SecretKey key = new SecretKeySpec(
                        Base64.getDecoder().decode(base64Secret),
                        SignatureAlgorithm.HS512.getJcaName()
        );
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS512).build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        JwtGrantedAuthoritiesConverter authConverter = new JwtGrantedAuthoritiesConverter();
        authConverter.setAuthoritiesClaimName("authorities");
        authConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authConverter);
        return converter;
    }


    @Bean
    public AuthenticationManager apiKeyAuthenticationManager(ApiKeyAuthenticationProvider apiKeyProvider)
    {
        return new ProviderManager(apiKeyProvider);
    }


    @Bean
    public ApiKeyAuthFilter apiKeyAuthFilter(AuthenticationManager apiKeyAuthenticationManager)
    {
        return new ApiKeyAuthFilter(apiKeyAuthenticationManager);
    }


    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer()
    {
        return cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            //config.setAllowedOrigins(Arrays.asList("*"));
            config.setAllowedOriginPatterns(Arrays.asList("*"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "websocket", "ws"));
            config.setAllowedHeaders(Arrays.asList("*"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        });
    }


    @Bean
    public Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer()
    {
        return csrf -> csrf.disable();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder, JwtAuthenticationConverter authConverter, ApiKeyAuthenticationProvider apiKeyProvider, ApiKeyAuthFilter apiKeyAuthFilter) throws Exception
    {
        http.cors(corsCustomizer())
                        .csrf(csrfCustomizer())
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(apiKeyProvider)
                        .addFilterBefore(apiKeyAuthFilter, BearerTokenAuthenticationFilter.class)
                        .authorizeHttpRequests(authorize -> authorize.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                        .requestMatchers("/health/**", "/api/**", "/v1/**").permitAll()
                                        .anyRequest().authenticated())
                        .oauth2ResourceServer(oauth2 -> oauth2
                                        .jwt(jwt -> jwt
                                                        .decoder(jwtDecoder)
                                                        .jwtAuthenticationConverter(authConverter)
                                        )
                        );
        return http.build();
    }
}
