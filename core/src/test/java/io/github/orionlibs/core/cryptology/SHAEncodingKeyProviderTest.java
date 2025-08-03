package io.github.orionlibs.core.cryptology;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SHAEncodingKeyProviderTest
{
    @Autowired SHAEncodingKeyProvider shaEncodingKeyProvider;


    @Test
    void testSHAEncodingKey()
    {
        byte[] key = shaEncodingKeyProvider.loadKey();
        assertThat(key).isNotNull();
        assertThat(key.length).isEqualTo(32);
    }
}
