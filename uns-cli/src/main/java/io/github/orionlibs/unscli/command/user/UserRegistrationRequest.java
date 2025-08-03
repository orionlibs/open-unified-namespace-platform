package io.github.orionlibs.unscli.command.user;

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
    private String username;
    private String password;
    private String authority;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
