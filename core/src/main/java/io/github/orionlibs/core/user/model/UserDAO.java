package io.github.orionlibs.core.user.model;

import java.util.Optional;
import java.util.UUID;

public interface UserDAO
{
    Optional<UserModel> findByUserID(UUID userID);


    Optional<UserModel> findByUsername(String username);


    Optional<UserModel> findByUsernameHash(String usernameHash);


    void deleteAll();


    UserModel save(UserModel model);
}
