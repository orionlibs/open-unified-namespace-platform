package io.github.orionlibs.user;

import io.github.orionlibs.core.Logger;
import io.github.orionlibs.core.user.UserService;
import io.github.orionlibs.core.user.model.UserModel;
import io.github.orionlibs.user.api.UpdatePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordService
{
    @Autowired
    private UserService userService;


    @Transactional
    public boolean update(String userID, UpdatePasswordRequest request)
    {
        UserModel user = userService.loadUserByUserID(userID);
        user.setPassword(request.getPassword());
        userService.saveUser(user);
        Logger.info("Updated user password");
        return true;
    }
}
