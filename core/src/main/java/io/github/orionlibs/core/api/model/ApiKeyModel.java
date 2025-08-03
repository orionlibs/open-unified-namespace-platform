package io.github.orionlibs.core.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "api_keys", schema = "uns", indexes = {
                @Index(name = "idx_uns_api_keys", columnList = "apiKey,user_id")
})
public class ApiKeyModel
{
    @Id
    @Column(name = "api_key", nullable = false, updatable = false)
    private String apiKey;
    @Column(name = "api_secret", nullable = false)
    private String apiSecret;
    @Column(name = "user_id", nullable = false)
    private UUID userID;


    public String getApiKey()
    {
        return apiKey;
    }


    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }


    public String getApiSecret()
    {
        return apiSecret;
    }


    public void setApiSecret(String apiSecret)
    {
        this.apiSecret = apiSecret;
    }


    public UUID getUserID()
    {
        return userID;
    }


    public void setUserID(UUID userID)
    {
        this.userID = userID;
    }
}
