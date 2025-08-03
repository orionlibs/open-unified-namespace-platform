package io.github.orionlibs.core.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.orionlibs.core.jwt.JWTService;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class JWTServiceTest
{
    static final String RAW_SIGNING_KEY = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
    static final String SIGNING_KEY_BASE64 = Base64.getEncoder().encodeToString(RAW_SIGNING_KEY.getBytes(StandardCharsets.UTF_8));
    @Autowired JWTService jwtService;


    @Test
    void convertSigningKeyToSecretKeyObject()
    {
        Key key = jwtService.convertSigningKeyToSecretKeyObject(SIGNING_KEY_BASE64);
        assertThat(key).isNotNull();
        assertThat(key).isInstanceOf(SecretKey.class);
        assertThat(key.getAlgorithm()).isEqualTo(SignatureAlgorithm.HS512.getJcaName());
    }


    @Test
    void generateToken_extractUsername_shouldMatchUser()
    {
        UserDetails user = new User(
                        "Jimmy",
                        "4528",
                        List.of(new SimpleGrantedAuthority("USER"))
        );
        String token = jwtService.generateToken(user);
        assertThat(token).isNotNull();
        String extracted = jwtService.extractUsername(token);
        assertThat(extracted).isEqualTo("Jimmy");
    }


    @Test
    void validateToken_validToken()
    {
        UserDetails user = new User(
                        "Jimmy",
                        "4528",
                        List.of(new SimpleGrantedAuthority("USER"))
        );
        String token = jwtService.generateToken(user);
        assertThat(token).isNotNull();
        assertThat(jwtService.validateToken(token, user)).isTrue();
    }


    @Test
    void validateToken_wrongUser()
    {
        UserDetails user1 = new User(
                        "Jimmy",
                        "4528",
                        List.of(new SimpleGrantedAuthority("USER"))
        );
        UserDetails user2 = new User(
                        "James",
                        "4528",
                        List.of(new SimpleGrantedAuthority("USER"))
        );
        String token = jwtService.generateToken(user1);
        assertThat(token).isNotNull();
        assertThat(jwtService.validateToken(token, user2)).isFalse();
    }


    @Test
    void validateToken_expiredToken()
    {
        Date now = new Date();
        Date past = new Date(now.getTime() - 10_000);
        UserDetails user = new User(
                        "Jimmy",
                        "4528",
                        List.of(new SimpleGrantedAuthority("USER"))
        );
        String expiredToken = jwtService.generateToken(user, new Date(now.getTime() - 20_000), past);
        assertThat(expiredToken).isNotNull();
        assertThat(jwtService.validateToken(expiredToken, user)).isFalse();
    }
}
