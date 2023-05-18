package com.genius.config;

import com.genius.core.MqPool;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Genius
 * @date 2023/05/12 18:57
 **/
@Configuration
@AutoConfigureAfter(SystemConfig.class)
public class MqConfig {

    @Bean
    public DirectExchange nonjronTaskDirectExchange(){
        return new DirectExchange(MqPool.EXCHANGE_TOPIC_NONJRON_TASK);
    }

    @Bean
    public Queue taskSendQueue(){
        return new Queue(MqPool.MASTER_TASK_SEND_CENTER);
    }


    @Bean
    public Queue taskReplyQueue(){
        return new Queue( String.format("task.%s.reply",SystemConfig.ServiceId));

    }

    @Bean
    public Binding bindingReplyQueue(Queue taskReplyQueue,DirectExchange nonjronTaskDirectExchange){
        return BindingBuilder.bind(taskReplyQueue).to(nonjronTaskDirectExchange).with(SystemConfig.ServiceId);
    }

}
