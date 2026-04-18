package org.puregxl.site.infra;

import org.junit.jupiter.api.Test;
import org.puregxl.site.infra.config.AIModelProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {
    @Autowired
    AIModelProperties aiModelProperties;
    @Test
    void test() {
        System.out.println(aiModelProperties);
        System.out.println("explain");
    }
}
