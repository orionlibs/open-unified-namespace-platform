package io.github.orionlibs.user.registration;

import io.github.orionlibs.core.Logger;
import io.github.orionlibs.core.cryptology.HMACSHAEncryptionKeyProvider;
import io.github.orionlibs.core.data.DuplicateRecordException;
import io.github.orionlibs.core.event.Publishable;
import io.github.orionlibs.core.user.UserService;
import io.github.orionlibs.user.event.EventUserRegistered;
import io.github.orionlibs.core.user.model.UserModel;
import io.github.orionlibs.user.registration.api.UserRegistrationRequest;
import io.github.orionlibs.user.setting.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService implements Publishable
{
    @Autowired
    private UserService userService;
    @Autowired
    private UserSettingsService userSettingsService;
    @Autowired
    private HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;


    @Transactional
    public void registerUser(UserRegistrationRequest request) throws DuplicateRecordException
    {
        UserModel newUser = new UserModel(hmacSHAEncryptionKeyProvider);
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setAuthority(request.getAuthority());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        try
        {
            userService.saveUser(newUser);
            userSettingsService.saveDefaultSettingsForUser(newUser);
            publish(EventUserRegistered.EVENT_NAME, EventUserRegistered.builder()
                            .username(request.getUsername())
                            .build());
            Logger.info("User saved");
        }
        catch(DataIntegrityViolationException | UnexpectedRollbackException e)
        {
            throw new DuplicateRecordException(e, "This user already exists");
        }
    }
}
