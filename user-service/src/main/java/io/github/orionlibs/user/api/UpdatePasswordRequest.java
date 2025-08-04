package io.github.orionlibs.user.api;

import io.github.orionlibs.core.user.Password;
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
public class UpdatePasswordRequest
{
    @Password
    private String password;
}
