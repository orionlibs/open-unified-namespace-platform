package io.github.orionlibs.core.user.model;

import io.github.orionlibs.core.cryptology.HMACSHAEncryptionKeyProvider;
import io.github.orionlibs.core.cryptology.SHAEncodingKeyProvider;
import io.github.orionlibs.core.user.setting.model.UserSettingsModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users", schema = "uns", indexes = {
                @Index(name = "idx_uns_users", columnList = "id,username")
},
                uniqueConstraints = @UniqueConstraint(name = "uq_users_username", columnNames = "username_hash"))
public class UserModel implements UserDetails
{
    @Transient
    private HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false)
    @Convert(converter = AesGcmColumnConverter.class)
    private String username;
    @Column(name = "username_hash", length = 300, nullable = false, updatable = false)
    private String usernameHash;
    @Column(nullable = false)
    @Convert(converter = PasswordColumnConverter.class)
    private String password;
    @Column(nullable = false)
    private String authority;
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;
    @Column(name = "first_name", nullable = false)
    @Convert(converter = AesGcmColumnConverter.class)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @Convert(converter = AesGcmColumnConverter.class)
    private String lastName;
    @Column(name = "phone_number")
    @Convert(converter = AesGcmColumnConverter.class)
    private String phoneNumber;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @OneToMany(
                    mappedBy = "user",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true
    )
    private List<UserSettingsModel> settings = new ArrayList<>();


    public UserModel()
    {
        isEnabled = true;
    }


    public UserModel(HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider)
    {
        this();
        this.hmacSHAEncryptionKeyProvider = hmacSHAEncryptionKeyProvider;
    }


    public String getAuthority()
    {
        return authority;
    }


    public void setUsername(String username)
    {
        this.username = username;
        this.usernameHash = hmacSHAEncryptionKeyProvider.getNewHMACBase64(username, SHAEncodingKeyProvider.shaKey);
    }


    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }


    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }


    public UUID getId()
    {
        return id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Arrays.stream(authority.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
    }


    @Override
    public String getPassword()
    {
        return password;
    }


    @Override
    public String getUsername()
    {
        return username;
    }


    @Override
    public boolean isAccountNonExpired()
    {
        return UserDetails.super.isAccountNonExpired();
    }


    @Override
    public boolean isAccountNonLocked()
    {
        return UserDetails.super.isAccountNonLocked();
    }


    @Override
    public boolean isCredentialsNonExpired()
    {
        return UserDetails.super.isCredentialsNonExpired();
    }


    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public void setAuthority(String authority)
    {
        this.authority = authority;
    }


    public void setEnabled(boolean enabled)
    {
        isEnabled = enabled;
    }


    public List<UserSettingsModel> getSettings()
    {
        return settings;
    }


    public String getFirstName()
    {
        return firstName;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getLastName()
    {
        return lastName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getPhoneNumber()
    {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
