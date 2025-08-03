package io.github.orionlibs.core.api;

import io.github.orionlibs.core.api.model.ApiKeyDAO;
import io.github.orionlibs.core.api.model.ApiKeyModel;
import io.github.orionlibs.core.user.model.UserDAO;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyValidationService
{
    @Autowired
    private ApiKeyDAO apiKeyDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserDetailsService userService;


    public UserDetails validate(String apiKey, String apiSecret) throws AuthenticationException
    {
        Optional<ApiKeyModel> opt = apiKeyDAO.findById(apiKey);
        if(opt.isEmpty())
        {
            return null;
        }
        ApiKeyModel model = opt.get();
        if(!model.getApiSecret().equals(apiSecret))
        {
            return null;
        }
        return userDAO.findByUserID(model.getUserID())
                        .map(user -> userService.loadUserByUsername(user.getUsername()))
                        .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
}
