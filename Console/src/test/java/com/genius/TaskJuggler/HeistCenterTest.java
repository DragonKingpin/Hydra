package com.genius.TaskJuggler;

import com.genius.Ampq.RabbitMQTest;
import com.genius.common.UlfUMC.CommonMessageBuilder;
import com.genius.common.UlfUMC.UlfUMCMessage;
import com.genius.common.UlfUMC.UlfUMCMessageException;
import com.genius.common.UlfUMC.UlfUMCMessageType;
import com.genius.config.SystemConfig;
import com.genius.pool.FunctionNamePool;
import com.genius.core.HeistCenter;
import com.genius.pool.MqPool;
import org.junit.Test;
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
    public void master(byte[] arr) throws UlfUMCMessageException {
        UlfUMCMessage msg = UlfUMCMessage.decode(arr);
        logger.info("slave<{}> want to rob the {}",msg.getUlfUMCBody().getData().get("serviceId"),msg.getUlfUMCBody().getData().get("task"));
        UlfUMCMessage message;
        if(count>=500){
            CommonMessageBuilder commonMessageBuilder = new CommonMessageBuilder();
            message = commonMessageBuilder.func(FunctionNamePool.SHUTDOWN).method(UlfUMCMessageType.GET).build();
        }else{
            CommonMessageBuilder commonMessageBuilder = new CommonMessageBuilder();
            message = commonMessageBuilder.func(FunctionNamePool.QUERY_TASK_RANGE)
                    .method(UlfUMCMessageType.GET)
                    .data(Map.of("lowLimit", count, "upLimit", count + 100)).build();

        }
        count+=100;
        rabbitTemplate.convertAndSend(MqPool.EXCHANGE_TOPIC_NONJRON_TASK, SystemConfig.ServiceId,UlfUMCMessage.encode(message));
    }

    @Test
    public void testHeistCenter() throws InterruptedException {
        heistCenter.start();
    }
}
