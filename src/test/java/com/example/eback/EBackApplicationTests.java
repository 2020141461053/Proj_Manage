package com.example.eback;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class EBackApplicationTests {


    @Autowired
    private ApplicationContext context;

    @Test
    void test() {
        assertNotNull(context);
    }

}
