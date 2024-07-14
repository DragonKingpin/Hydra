package com.genius.config;

import com.genius.constpool.RadiumConstPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Genius
 * @date 2023/05/13 16:14
 **/

@Data
@Configuration
@ConfigurationProperties(RadiumConstPool.CONFIG_SYSTEM_CONFIG_PREFIX+"tracer")
public class LoggerConfig {

    public static boolean ConsoleTrace;

    public String InfoTracer;

    public static String ErrTracer;


    @Value("${"+ RadiumConstPool.CONFIG_SYSTEM_CONFIG_TRACER_PREFIX +"consoleTrace}")
    private boolean consoleTrace;

    @Value("${"+ RadiumConstPool.CONFIG_SYSTEM_CONFIG_TRACER_PREFIX +"infoTracer}")
    private String infoTracer;

    @Value("${"+ RadiumConstPool.CONFIG_SYSTEM_CONFIG_TRACER_PREFIX +"errTracer}")
    private String errTracer;

    @PostConstruct
    public void config(){
        ConsoleTrace = consoleTrace;
        InfoTracer = String.format(infoTracer,SystemConfig.ServiceId);
        ErrTracer = String.format(errTracer,SystemConfig.ServiceId);
    }
}
