package com.genius.Logger;

import com.genius.config.LoggerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Genius
 * @date 2023/05/13 16:27
 **/
@SpringBootTest
public class LoggerConfigTest {

    @Test
    public void testConfig(){
        System.out.println(LoggerConfig.ErrTracer);
    }
}
