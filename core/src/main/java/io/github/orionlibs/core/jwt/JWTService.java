package io.github.orionlibs.core.jwt;

import io.github.orionlibs.core.cryptology.HMACSHAEncryptionKeyProvider;
import io.github.orionlibs.core.user.UserService;
import io.github.orionlibs.core.user.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTService
{
    private static final long EXPIRATION_IN_MILLISECONDS = 3_600_000L;
    @Autowired
    private UserService userService;
    @Autowired
    private HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;


    public Key convertSigningKeyToSecretKeyObject(String signingKey)
    {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(signingKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
    }


    public String generateToken(UserDetails userDetails)
    {
        UserModel user = userService.loadUserAsModelByUsername(userDetails.getUsername());
        return Jwts.builder()
                        .setSubject(user.getId().toString())
                        .claim("authorities", userDetails.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_MILLISECONDS))
                        .signWith(convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey()), SignatureAlgorithm.HS512)
                        .compact();
    }


    public String generateToken(String userID, Collection<? extends GrantedAuthority> authorities)
    {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_IN_MILLISECONDS);
        return Jwts.builder()
                        .setSubject(userID)
                        .claim("authorities", authorities
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(now)
                        .expiration(expirationDate)
                        .signWith(convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey()), SignatureAlgorithm.HS512)
                        .compact();
    }


    public String generateToken(UserDetails userDetails, Date issuedAt, Date expiresAt)
    {
        UserModel user = userService.loadUserAsModelByUsername(userDetails.getUsername());
        return Jwts.builder()
                        .setSubject(user.getId().toString())
                        .claim("authorities", userDetails.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(issuedAt)
                        .expiration(expiresAt)
                        .signWith(convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey()), SignatureAlgorithm.HS512)
                        .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails)
    {
        String userID = extractUserID(token);
        UserModel user = userService.loadUserAsModelByUsername(userDetails.getUsername());
        boolean isTokenExpired = false;
        try
        {
            isTokenExpired = isTokenExpired(token);
        }
        catch(ExpiredJwtException e)
        {
            isTokenExpired = true;
        }
        catch(Exception e)
        {
            isTokenExpired = false;
        }
        return userID.equals(user.getId().toString()) && !isTokenExpired;
    }


    public String extractUserID(String token)
    {
        boolean isTokenExpired = false;
        try
        {
            isTokenExpired = isTokenExpired(token);
        }
        catch(ExpiredJwtException e)
        {
            isTokenExpired = true;
        }
        catch(Exception e)
        {
            isTokenExpired = false;
        }
        if(isTokenExpired)
        {
            return "INVALID-USER-ID";
        }
        else
        {
            Claims claims = parseClaims(token);
            return claims.getSubject();
        }
    }


    private boolean isTokenExpired(String token)
    {
        try
        {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        }
        catch(ExpiredJwtException e)
        {
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }


    private Claims parseClaims(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, SecurityException, IllegalArgumentException
    {
        SecretKey key = (SecretKey)convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey());
        return Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseClaimsJws(token)
                        .getPayload();
    }
        /*ExpiredJwtException
        UnsupportedJwtException
        MalformedJwtException
        SignatureException
        SecurityException
        IllegalArgumentException*/
}
