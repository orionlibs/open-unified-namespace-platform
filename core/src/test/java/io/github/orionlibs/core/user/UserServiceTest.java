package io.github.orionlibs.core.user;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.cryptology.HMACSHAEncryptionKeyProvider;
import io.github.orionlibs.core.user.model.UserDAO;
import io.github.orionlibs.core.user.model.UserModel;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest
{
    @Autowired UserDAO dao;
    @Autowired UserService userService;
    @Autowired HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;


    @BeforeEach
    void setup()
    {
        dao.deleteAll();
    }


    @Test
    void loadUserByUsername()
    {
        UserModel userTemp = new UserModel(hmacSHAEncryptionKeyProvider);
        userTemp.setUsername("me@email.com");
        userTemp.setPassword("4528");
        userTemp.setAuthority("USER");
        userTemp.setFirstName("Dimi");
        userTemp.setLastName("Emilson");
        userTemp.setPhoneNumber("07896620211");
        UserModel newUser = dao.save(userTemp);
        assertThat(newUser.getCreatedAt()).isNotNull();
        assertThat(newUser.getUpdatedAt()).isNotNull();
        UserDetails user = userService.loadUserByUsername("me@email.com");
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("me@email.com");
        assertThat(user.getPassword().isEmpty()).isFalse();
        assertThat(user.getAuthorities()).isEqualTo(Set.of(new SimpleGrantedAuthority("USER")));
    }
}
