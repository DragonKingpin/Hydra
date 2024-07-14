package com.genius.Ampq;

import com.genius.config.SystemConfig;
import com.genius.mq.Harbor;
import com.genius.pool.FunctionNamePool;
import com.genius.pool.MqPool;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.Map;


/**
 * @author Genius
 * @date 2023/05/09 14:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Harbor harbor;

    Logger logger = LoggerFactory.getLogger(RabbitMQTest.class);

    @Data
    class Teacher implements Serializable {
        private String name;
        private String clazz;
        private int age;

        public Teacher(String name, String clazz, int age) {
            this.name = name;
            this.clazz = clazz;
            this.age = age;
        }
    }

}
