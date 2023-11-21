package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class ApplicationContextUnitTest {

    @Test
    void shouldLoadContext() {
        Assertions.assertTrue(true);
    }

}
