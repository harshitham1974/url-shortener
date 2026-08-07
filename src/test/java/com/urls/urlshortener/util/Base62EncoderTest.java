package com.urls.urlshortener.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    @Test
    void encodesZeroAsFirstCharacter() {
        assertEquals("0", Base62Encoder.encode(0));
    }

    @Test
    void encodesDifferentNumbersToDifferentCodes() {
        String code1 = Base62Encoder.encode(1);
        String code2 = Base62Encoder.encode(2);
        assertNotEquals(code1, code2);
    }

    @Test
    void encodingIsConsistent() {
        assertEquals(Base62Encoder.encode(12345), Base62Encoder.encode(12345));
    }
}
