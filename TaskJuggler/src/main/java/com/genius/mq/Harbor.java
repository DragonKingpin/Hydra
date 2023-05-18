package com.genius.mq;

import com.genius.common.Message;
import com.genius.common.MessageType;
import com.genius.config.SystemConfig;
import com.genius.core.FunctionNamePool;
import com.genius.core.MqPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author Genius
 * @date 2023/05/12 18:08
 **/
@Lazy
@Component
public class Harbor {

    @Resource
    RabbitTemplate mqPublisher;

    private BlockingDeque<Message> messageQueue = new LinkedBlockingDeque<>();

    private Logger logger = LoggerFactory.getLogger("<Harbor>");

    public void stockWithGoods(String goodsName){
        Message message = new Message();

        message.setMethod(MessageType.QUERY);
        message.setFunction(FunctionNamePool.QUERY_TASK_RANGE);
        message.setData(Map.of(
                "task", goodsName,
                "serviceId",SystemConfig.ServiceId
        ));

        mqPublisher.convertAndSend(MqPool.MASTER_TASK_SEND_CENTER,message);
    }

    //TODO need MQ confirm to optimize Message robustness
    @RabbitListener(queues = "#{taskReplyQueue.name}")
    private void getSpoilFromMaster(Message msg){
        if(!Objects.isNull(msg)){
            logger.info("Get instructions from the boss :{}",msg);
            messageQueue.add(msg);
        }
    }

    public Message getSpoil(String name) throws InterruptedException {
        Message msg = messageQueue.poll(10L, TimeUnit.SECONDS);
        if(msg == null){
            stockWithGoods(name);
            return null;
        }
        return msg;
    }

}
