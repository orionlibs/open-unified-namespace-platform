package io.github.orionlibs.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private ApiKeyValidationService validationService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        ApiKeyAuthenticationToken token = (ApiKeyAuthenticationToken)authentication;
        UserDetails user = validationService.validate(token.getApiKey(), token.getApiSecret());
        if(user == null)
        {
            throw new BadCredentialsException("Invalid API key/secret");
        }
        return new ApiKeyAuthenticationToken(
                        user, user.getAuthorities(),
                        token.getApiKey(),
                        token.getApiSecret()
        );
    }


    @Override
    public boolean supports(Class<?> authentication)
    {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
