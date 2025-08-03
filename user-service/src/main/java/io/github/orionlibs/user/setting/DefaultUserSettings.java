package io.github.orionlibs.user.setting;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user.default-settings")
public class DefaultUserSettings
{
    private List<Setting> settings = new ArrayList<>();


    public List<Setting> getSettings()
    {
        return settings;
    }


    public void setSettings(List<Setting> settings)
    {
        this.settings = settings;
    }


    public static class Setting
    {
        private String name;
        private String value;


        public String getName()
        {
            return name;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        public String getValue()
        {
            return value;
        }


        public void setValue(String value)
        {
            this.value = value;
        }
    }
}
