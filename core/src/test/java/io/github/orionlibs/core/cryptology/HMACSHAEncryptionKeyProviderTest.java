package io.github.orionlibs.core.cryptology;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class HMACSHAEncryptionKeyProviderTest
{
    @Autowired SHAEncodingKeyProvider shaEncodingKeyProvider;
    @Autowired HMACSHAEncryptionKeyProvider hmacSHAEncryptionKeyProvider;


    @Test
    void testAESEncryptionKey()
    {
        String key = hmacSHAEncryptionKeyProvider.getNewHMACBase64("sometest", shaEncodingKeyProvider.loadKey());
        assertThat(key.length()).isGreaterThan(10);
    }
}
