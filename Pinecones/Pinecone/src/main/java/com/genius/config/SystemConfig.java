package com.genius.config;

import com.genius.constpool.RadiumConstPool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Genius
 * @date 2023/05/12 18:44
 **/
@Data
@Configuration
public class SystemConfig {


    public static String MinionName;
    public static String ServiceId;

    @Value("${"+ RadiumConstPool.CONFIG_SYSTEM_CONFIG_PREFIX +"serviceId}")
    private String serviceId;

    @Value("${"+ RadiumConstPool.CONFIG_SYSTEM_CONFIG_PREFIX +"minionName}")
    private String minionName;

    @PostConstruct
    public void config(){
        MinionName =  minionName;
        ServiceId = serviceId;
    }

}
