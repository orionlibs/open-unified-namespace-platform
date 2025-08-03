package io.github.orionlibs.user.authentication.api;

import io.github.orionlibs.core.user.Password;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginRequest
{
    @Email(message = "Invalid email address format")
    private String username;
    @Password
    private String password;
}
