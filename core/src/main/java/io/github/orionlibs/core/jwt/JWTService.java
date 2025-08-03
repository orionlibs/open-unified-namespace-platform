package io.github.orionlibs.core.jwt;

import io.github.orionlibs.core.cryptology.HMACSHAEncryptionKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private static final long EXPIRATION_IN_MILLISECONDS = 3600000;
    @Autowired
    private HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;


    public Key convertSigningKeyToSecretKeyObject(String signingKey)
    {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(signingKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());
    }


    public String generateToken(UserDetails userDetails)
    {
        return Jwts.builder()
                        .subject(userDetails.getUsername())
                        .claim("authorities", userDetails.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_MILLISECONDS))
                        .signWith(convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey()), SignatureAlgorithm.HS512)
                        .compact();
    }


    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities)
    {
        return Jwts.builder()
                        .subject(username)
                        .claim("authorities", authorities
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_MILLISECONDS))
                        .signWith(convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey()), SignatureAlgorithm.HS512)
                        .compact();
    }


    public String generateToken(UserDetails userDetails, Date issuedAt, Date expiresAt)
    {
        return Jwts.builder()
                        .subject(userDetails.getUsername())
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
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    public String extractUsername(String token)
    {
        try
        {
            Claims claims = parseClaims(token);
            return claims.getSubject();
        }
        catch(Exception e)
        {
            return "";
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


    private Claims parseClaims(String token)
    {
        SecretKey key = (SecretKey)convertSigningKeyToSecretKeyObject(hmacSHAEncryptionKeyProvider.getJwtSigningKey());
        return Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        /*ExpiredJwtException
        UnsupportedJwtException
        MalformedJwtException
        SignatureException
        SecurityException
        IllegalArgumentException*/
    }
}
