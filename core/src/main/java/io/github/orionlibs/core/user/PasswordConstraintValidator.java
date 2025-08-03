package io.github.orionlibs.core.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<Password, String>
{
    private final Pattern regex;


    public PasswordConstraintValidator(Environment env)
    {
        String pattern = env.getProperty("password.pattern", "^(?=.{8,})(?=.*[A-Z])(?=.*\\d).*$");
        this.regex = Pattern.compile(pattern);
    }


    @Override
    public boolean isValid(String raw, ConstraintValidatorContext context)
    {
        if(raw == null || raw.isBlank())
        {
            return false;
        }
        return regex.matcher(raw).matches();
    }
}
