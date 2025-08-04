package io.github.orionlibs.user.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.orionlibs.core.data.DuplicateRecordException;
import io.github.orionlibs.core.user.model.UserDAO;
import io.github.orionlibs.core.user.model.UserModel;
import io.github.orionlibs.user.registration.api.UserRegistrationRequest;
import io.github.orionlibs.user.setting.UserSettingsService;
import io.github.orionlibs.core.user.setting.model.UserSettingsModel;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserRegistrationServiceTest
{
    @Autowired UserDAO dao;
    @Autowired UserRegistrationService userRegistrationService;
    @Autowired UserSettingsService userSettingsService;


    @BeforeEach
    void setup()
    {
        dao.deleteAll();
    }


    @Test
    void registerUser()
    {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("4528")
                        .authority("USER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        userRegistrationService.registerUser(request);
        UserModel user = dao.findByUsername("me@email.com").get();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("me@email.com");
        assertThat(user.getPassword().isEmpty()).isFalse();
        assertThat(user.getFirstName()).isEqualTo("Dimi");
        assertThat(user.getLastName()).isEqualTo("Emilson");
        assertThat(user.getPhoneNumber()).isEqualTo("07896620211");
        assertThat(user.getAuthority()).isEqualTo("USER");
        assertThat(user.getAuthorities()).isEqualTo(Set.of(new SimpleGrantedAuthority("USER")));
        List<UserSettingsModel> settings = userSettingsService.getByUserID(user.getId());
        assertThat(settings).isNotNull();
        assertThat(settings.size()).isEqualTo(1);
        assertThat(settings.get(0).getSettingName()).isEqualTo("theme");
        assertThat(settings.get(0).getSettingValue()).isEqualTo("dark");
    }


    @Test
    void registerUser_duplicateUser()
    {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username("me@email.com")
                        .password("4528")
                        .authority("USER")
                        .firstName("Dimi")
                        .lastName("Emilson")
                        .phoneNumber("07896620211")
                        .build();
        userRegistrationService.registerUser(request);
        assertThatThrownBy(() -> userRegistrationService.registerUser(request)).isInstanceOf(DuplicateRecordException.class)
                        .hasMessage("This user already exists");
    }
}
