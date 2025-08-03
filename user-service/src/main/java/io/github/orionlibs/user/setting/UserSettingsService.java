package io.github.orionlibs.user.setting;

import io.github.orionlibs.core.Logger;
import io.github.orionlibs.core.user.model.UserModel;
import io.github.orionlibs.core.user.setting.model.UserSettingsDAORepository;
import io.github.orionlibs.core.user.setting.model.UserSettingsModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSettingsService
{
    @Autowired
    private UserSettingsDAORepository dao;
    @Autowired
    private DefaultUserSettings defaultUserSettings;


    @Transactional(readOnly = true)
    public Optional<UserSettingsModel> getByID(UUID settingID)
    {
        return dao.findById(settingID);
    }


    @Transactional(readOnly = true)
    public List<UserSettingsModel> getByUserID(UUID userID)
    {
        return dao.findAllByUserId(userID);
    }


    @Transactional
    public UserSettingsModel save(UserSettingsModel model)
    {
        UserSettingsModel saved = dao.saveAndFlush(model);
        Logger.info("User setting saved");
        return saved;
    }


    @Transactional
    public boolean update(UserSettingsModel model)
    {
        UserSettingsModel updated = save(model);
        Logger.info("Updated user setting");
        return true;
    }


    @Transactional
    public void saveDefaultSettingsForUser(UserModel user)
    {
        for(DefaultUserSettings.Setting def : defaultUserSettings.getSettings())
        {
            UserSettingsModel s = new UserSettingsModel(user, def.getName(), def.getValue());
            save(s);
        }
        Logger.info("Saved default user settings for user");
    }
}
