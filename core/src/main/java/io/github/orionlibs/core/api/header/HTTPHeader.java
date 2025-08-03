package io.github.orionlibs.core.api.header;

import io.github.orionlibs.core.OrionEnumeration;

public enum HTTPHeader implements OrionEnumeration
{
    Referer("referer"),
    UserAgent("User-Agent"),
    WAPProfile("x-wap-profile"),
    XAPIKey("x-api-key"),
    XAPISecret("x-api-secret"),
    Profile("Profile"),
    Accept("Accept"),
    Authorization("Authorization"),
    NeedsReauthentication("needs-reauthentication");
    private String name;


    private HTTPHeader(String name)
    {
        setName(name);
    }


    @Override
    public String get()
    {
        return getName();
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    @Override
    public boolean is(OrionEnumeration other)
    {
        return other instanceof HTTPHeader && this == other;
    }


    @Override
    public boolean isNot(OrionEnumeration other)
    {
        return other instanceof HTTPHeader && this != other;
    }


    public static boolean valueExists(String other)
    {
        HTTPHeader[] values = values();
        for(HTTPHeader value : values)
        {
            if(value.get().equals(other))
            {
                return true;
            }
        }
        return false;
    }


    public static HTTPHeader getEnumForValue(String other)
    {
        HTTPHeader[] values = values();
        for(HTTPHeader value : values)
        {
            if(value.get().equals(other))
            {
                return value;
            }
        }
        return null;
    }
}