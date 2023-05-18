package com.genius.TaskJuggler;

import com.genius.Ampq.RabbitMQTest;
import com.genius.common.Message;
import com.genius.common.MessageType;
import com.genius.config.SystemConfig;
import com.genius.core.FunctionNamePool;
import com.genius.core.HeistCenter;
import com.genius.core.MqPool;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/09 13:59
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class HeistCenterTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    Logger logger = LoggerFactory.getLogger(RabbitMQTest.class);
    @Autowired
    HeistCenter heistCenter;

    int count = 0;

    @RabbitListener(queues= MqPool.MASTER_TASK_SEND_CENTER)
    public void master(Message msg){
        logger.info("slave<{}> want to rob the {}",msg.getData().get("serviceId"),msg.getData().get("task"));
        Message message;
        if(count>=500){
            message = new Message();
            message.setMethod(MessageType.SHUTDOWN);
        }else{
            message = new Message();
            message.setMethod(MessageType.REPLY);
            message.setFunction(FunctionNamePool.QUERY_TASK_RANGE);
            message.setData(Map.of("lowLimit",count,"upLimit",count+100));
        }
        count+=100;
        rabbitTemplate.convertAndSend(MqPool.EXCHANGE_TOPIC_NONJRON_TASK, SystemConfig.ServiceId,message);
    }

    @Test
    public void testHeistCenter() throws InterruptedException {
        heistCenter.start();
    }
}
