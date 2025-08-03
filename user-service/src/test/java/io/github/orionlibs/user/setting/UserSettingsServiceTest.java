package io.github.orionlibs.user.setting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.orionlibs.core.cryptology.HMACSHAEncryptionKeyProvider;
import io.github.orionlibs.core.user.model.UserDAO;
import io.github.orionlibs.core.user.model.UserModel;
import io.github.orionlibs.core.user.setting.model.UserSettingsDAORepository;
import io.github.orionlibs.core.user.setting.model.UserSettingsModel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserSettingsServiceTest
{
    @Autowired UserDAO userDAO;
    @Autowired UserSettingsDAORepository dao;
    @Autowired UserSettingsService userSettingsService;
    @Autowired HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;


    @BeforeEach
    void setup()
    {
        dao.deleteAll();
        userDAO.deleteAll();
    }


    @Test
    void saveUserSetting()
    {
        UserModel userModel = new UserModel(hmacSHAEncryptionKeyProvider);
        userModel.setUsername("me@email.com");
        userModel.setPassword("4528");
        userModel.setAuthority("USER");
        userModel.setFirstName("Dimi");
        userModel.setLastName("Emilson");
        userModel.setPhoneNumber("07896620211");
        UserModel newUser = userDAO.save(userModel);
        UserSettingsModel setting = new UserSettingsModel(newUser, "setting1", "yes");
        setting = userSettingsService.save(setting);
        assertThat(setting).isNotNull();
        assertThat(setting.getSettingName()).isEqualTo("setting1");
        assertThat(setting.getSettingValue()).isEqualTo("yes");
        assertThat(setting.getUser().getUsername()).isEqualTo("me@email.com");
        UserSettingsModel setting1 = userSettingsService.getByID(setting.getId()).get();
        assertThat(setting1).isNotNull();
        assertThat(setting1.getSettingName()).isEqualTo("setting1");
        assertThat(setting1.getSettingValue()).isEqualTo("yes");
        assertThat(setting1.getUser().getUsername()).isEqualTo("me@email.com");
    }


    @Test
    void saveUserSetting_noUserInDatabase()
    {
        UserModel userModel = new UserModel(hmacSHAEncryptionKeyProvider);
        userModel.setUsername("me@email.com");
        userModel.setPassword("4528");
        userModel.setAuthority("USER");
        userModel.setFirstName("Dimi");
        userModel.setLastName("Emilson");
        userModel.setPhoneNumber("07896620211");
        UserSettingsModel setting = new UserSettingsModel(userModel, "setting1", "yes");
        assertThatThrownBy(() -> userSettingsService.save(setting)).isInstanceOf(InvalidDataAccessApiUsageException.class)
                        .hasMessageContaining("TransientPropertyValueException: Not-null property references a transient value");
    }


    @Test
    void saveDefaultUserSettings()
    {
        UserModel userModel = new UserModel(hmacSHAEncryptionKeyProvider);
        userModel.setUsername("me@email.com");
        userModel.setPassword("4528");
        userModel.setAuthority("USER");
        userModel.setFirstName("Dimi");
        userModel.setLastName("Emilson");
        userModel.setPhoneNumber("07896620211");
        UserModel newUser = userDAO.save(userModel);
        userSettingsService.saveDefaultSettingsForUser(newUser);
        List<UserSettingsModel> settings = userSettingsService.getByUserID(newUser.getId());
        assertThat(settings).isNotNull();
        assertThat(settings.size()).isEqualTo(1);
        assertThat(settings.get(0).getSettingName()).isEqualTo("theme");
        assertThat(settings.get(0).getSettingValue()).isEqualTo("dark");
    }
}
