package com.genius.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Genius
 * @date 2023/05/12 15:44
 **/

@Configuration
public class MessageConverterConfig {

    @Bean
    public MessageConverter messageConverter() { return new Jackson2JsonMessageConverter();}
}
