package io.github.orionlibs.core.api;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken
{
    private final String apiKey;
    private final String apiSecret;


    public ApiKeyAuthenticationToken(String apiKey, String apiSecret)
    {
        super(null);
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        setAuthenticated(false);
    }


    public String getApiKey()
    {
        return apiKey;
    }


    public String getApiSecret()
    {
        return apiSecret;
    }


    public ApiKeyAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> auths, String apiKey, String apiSecret)
    {
        super(auths);
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        setAuthenticated(true);
        setDetails(principal);
    }


    @Override
    public Object getCredentials()
    {
        return apiSecret;
    }


    @Override
    public Object getPrincipal()
    {
        return getDetails();
    }
}
