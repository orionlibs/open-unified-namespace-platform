package io.github.orionlibs.core.user.setting.model;

import io.github.orionlibs.core.user.model.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user_settings", schema = "uns", indexes = {
                @Index(name = "idx_uns_user_settings", columnList = "id,user_id")
})
public class UserSettingsModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(name = "setting_name", nullable = false)
    private String settingName;
    @Column(name = "setting_value", nullable = false)
    private String settingValue;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(
                    name = "user_id",
                    nullable = false,
                    foreignKey = @ForeignKey(
                                    name = "fk_user_settings_user",
                                    foreignKeyDefinition =
                                                    "FOREIGN KEY (user_id) REFERENCES uns.users(id) ON DELETE CASCADE"
                    )
    )
    private UserModel user;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public UserSettingsModel()
    {
    }


    public UserSettingsModel(UserModel user, String settingName, String settingValue)
    {
        this.user = user;
        this.settingName = settingName;
        this.settingValue = settingValue;
    }


    public UUID getId()
    {
        return id;
    }


    public String getSettingName()
    {
        return settingName;
    }


    public void setSettingName(String settingName)
    {
        this.settingName = settingName;
    }


    public String getSettingValue()
    {
        return settingValue;
    }


    public void setSettingValue(String settingValue)
    {
        this.settingValue = settingValue;
    }


    public UserModel getUser()
    {
        return user;
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
