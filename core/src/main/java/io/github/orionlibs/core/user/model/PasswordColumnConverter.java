package io.github.orionlibs.core.user.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Converter
public class PasswordColumnConverter implements AttributeConverter<String, String>
{
    @Override
    public String convertToDatabaseColumn(String s)
    {
        if(s == null)
        {
            return null;
        }
        return new BCryptPasswordEncoder().encode(s);
    }


    @Override
    public String convertToEntityAttribute(String s)
    {
        return s;
    }
}
