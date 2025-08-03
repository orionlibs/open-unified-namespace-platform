package io.github.orionlibs.core.user.model;

import io.github.orionlibs.core.cryptology.AESEncryptionKeyProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * JPA AttributeConverter to encrypt/decrypt Strings with AES‑GCM/NoPadding
 */
@Converter
public class AesGcmColumnConverter implements AttributeConverter<String, String>
{
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;           // 96 bits
    private static final int TAG_LENGTH_BIT = 128;     // 128‑bit auth tag
    private SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();


    public AesGcmColumnConverter()
    {
        this.secretKey = AESEncryptionKeyProvider.dataEncryptionKey;
    }


    @Override
    public String convertToDatabaseColumn(String attribute)
    {
        if(attribute == null || attribute.isEmpty())
        {
            return null;
        }
        try
        {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            if(secretKey == null)
            {
                secretKey = AESEncryptionKeyProvider.dataEncryptionKey;
            }
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] cipherText = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            // Prepend IV to cipherText
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            byte[] ivAndCipher = byteBuffer.array();
            return Base64.getEncoder().encodeToString(ivAndCipher);
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Error encrypting attribute", e);
        }
    }


    @Override
    public String convertToEntityAttribute(String dbData)
    {
        if(dbData == null || dbData.isEmpty())
        {
            return null;
        }
        try
        {
            byte[] ivAndCipher = Base64.getDecoder().decode(dbData);
            ByteBuffer byteBuffer = ByteBuffer.wrap(ivAndCipher);
            byte[] iv = new byte[IV_LENGTH];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Error decrypting database column", e);
        }
    }
}
