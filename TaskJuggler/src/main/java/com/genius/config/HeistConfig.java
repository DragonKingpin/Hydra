package com.genius.config;

import com.genius.constpool.RadiumConstPool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Genius
 * @date 2023/05/09 13:06
 **/
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = RadiumConstPool.CONFIG_COMPONENTS_PREFIX+"heist")
public class HeistConfig {

    private Integer failureRetryTimes;

    private Integer heistNum;

    private Long exceptionWaitTime;  //线程池异常等待时间

    private String robTaskName; //执行的任务

}
