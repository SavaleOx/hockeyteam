package com.savaleox.hockeyteam;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HockeyApplicationTests.TestApplication.class)
class HockeyApplicationTests {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class TestApplication {
    }

}