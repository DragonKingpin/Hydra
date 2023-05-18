package com.genius.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Genius
 * @date 2023/05/12 18:44
 **/
@Data
@Configuration
public class SystemConfig {

    private static final String PREFIX = "nonjron.system.config.";

    public static String MinionName;
    public static String ServiceId;

    @Value("${"+PREFIX+"serviceId}")
    private String serviceId;

    @Value("${"+PREFIX+"minionName}")
    private String minionName;

    @PostConstruct
    public void config(){
        MinionName =  minionName;
        ServiceId = serviceId;
    }

}
