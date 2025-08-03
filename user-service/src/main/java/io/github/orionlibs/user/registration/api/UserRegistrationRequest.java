package io.github.orionlibs.user.registration.api;

import io.github.orionlibs.core.user.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserRegistrationRequest
{
    @Email(message = "Invalid email address format")
    private String username;
    @Password
    private String password;
    @NotBlank(message = "Authority must not be blank")
    private String authority;
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    @NotBlank(message = "Last name must not be blank")
    private String lastName;
    private String phoneNumber;
}
