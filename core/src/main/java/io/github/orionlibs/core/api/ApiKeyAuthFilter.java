package io.github.orionlibs.core.api;

import io.github.orionlibs.core.api.header.HTTPHeader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter
{
    private final AuthenticationManager authManager;


    public ApiKeyAuthFilter(AuthenticationManager authManager)
    {
        this.authManager = authManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        String accessKey = request.getHeader(HTTPHeader.XAPIKey.get());
        if(accessKey != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(accessKey, "");
            try
            {
                Authentication authResult = authManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
            catch(AuthenticationException ex)
            {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
