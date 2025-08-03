package io.github.orionlibs.core.cryptology;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESEncryptionKeyProvider implements InitializingBean
{
    public static SecretKey dataEncryptionKey;
    @Value("${crypto.aes256.key}")
    private String aes256Key;
    private byte[] KEY_BYTES_FOR_AES_256;


    @Override
    public void afterPropertiesSet() throws Exception
    {
        KEY_BYTES_FOR_AES_256 = aes256Key.getBytes(StandardCharsets.UTF_8);
        dataEncryptionKey = loadDataEncryptionKey();
    }


    // 32 bytes → AES‑256
    public SecretKey loadDataEncryptionKey()
    {
        // TODO: fetch the wrapped key from Vault/KMS, unwrap it, and return a SecretKey
        return new SecretKeySpec(KEY_BYTES_FOR_AES_256, "AES");
    }
}
