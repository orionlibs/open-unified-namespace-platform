package io.github.orionlibs.unscli;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class CredentialsProvider
{
    private String apiKey;
    private String apiSecret;


    @PostConstruct
    public void init()
    {
        Path credFile = Paths.get(System.getProperty("user.home"), ".uns", "credentials");
        if(!Files.exists(credFile))
        {
            throw new IllegalStateException("Credentials file not found at " + credFile);
        }
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(credFile))
        {
            props.load(in);
            apiKey = props.getProperty("uns_api_key");
            apiSecret = props.getProperty("uns_api_secret");
        }
        catch(IOException e)
        {
            throw new RuntimeException("Failed to load credentials", e);
        }
        if(apiKey == null || apiSecret == null)
        {
            throw new IllegalStateException("Missing API key or secret in credentials file");
        }
    }


    public String getApiKey()
    {
        return apiKey;
    }


    public String getApiSecret()
    {
        return apiSecret;
    }
}
