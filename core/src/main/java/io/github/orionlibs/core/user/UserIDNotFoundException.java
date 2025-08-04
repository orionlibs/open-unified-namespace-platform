package io.github.orionlibs.core.user;

import org.springframework.security.core.AuthenticationException;

public class UserIDNotFoundException extends AuthenticationException
{
    public UserIDNotFoundException(String message)
    {
        super(message);
    }


    public UserIDNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
