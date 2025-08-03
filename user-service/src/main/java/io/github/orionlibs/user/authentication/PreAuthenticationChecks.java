package io.github.orionlibs.user.authentication;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class PreAuthenticationChecks implements UserDetailsChecker
{
    @Override
    public void check(UserDetails user)
    {
        if(!user.isAccountNonLocked())
        {
            throw new LockedException("User account is locked");
        }
        if(!user.isEnabled())
        {
            throw new DisabledException("User account is disabled");
        }
        if(!user.isAccountNonExpired())
        {
            throw new AccountExpiredException("User account has expired");
        }
    }
}
