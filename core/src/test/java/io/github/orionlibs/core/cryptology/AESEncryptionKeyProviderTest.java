package io.github.orionlibs.core.cryptology;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AESEncryptionKeyProviderTest
{
    @Autowired AESEncryptionKeyProvider aesEncryptionKeyProvider;


    @Test
    void testAESEncryptionKey()
    {
        assertThat(aesEncryptionKeyProvider.loadDataEncryptionKey()).isNotNull();
    }
}
