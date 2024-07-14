package com.genius.mq;

import com.genius.common.UlfUMC.*;
import com.genius.pool.FunctionNamePool;
import com.genius.pool.MqPool;
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

    private BlockingDeque<UlfUMCMessage> messageQueue = new LinkedBlockingDeque<>();

    private Logger logger = LoggerFactory.getLogger("<Harbor>");

    public void stockWithGoods(String goodsName){

        UlfUMCMessage message = MessageFactory.getMessageBuilder(MessageFactory.MessageBuilderType.SLAVE)
                .method(UlfUMCMessageType.GET)
                .func(FunctionNamePool.QUERY_TASK_RANGE)
                .data(Map.of("task", goodsName)).build();


        mqPublisher.convertAndSend(MqPool.MASTER_TASK_SEND_CENTER,UlfUMCMessage.encode(message));
    }

    //TODO need MQ confirm to optimize Message robustness
    @RabbitListener(queues = "task.Nonaron-Kingpin-Prime.reply")
    private void getSpoilFromMaster(byte[] data){
        if(!Objects.isNull(data)){
            try {
                UlfUMCMessage msg = UlfUMCMessage.decode(data);
                logger.info("Get instructations from the boss :{}",msg);
                messageQueue.add(msg);
            }catch (UlfUMCMessageException e){
                ErrorMessageBuilder messageBuilder = (ErrorMessageBuilder) MessageFactory.getMessageBuilder(MessageFactory.MessageBuilderType.ERROR);
                messageQueue.add(messageBuilder.error(e.getMessage()).build());
            }
        }
    }

    public UlfUMCMessage getSpoil(String name) throws InterruptedException {
        UlfUMCMessage msg = messageQueue.poll(2L, TimeUnit.SECONDS);
        if(msg == null){
            stockWithGoods(name);
            return null;
        }
        return msg;
    }

}
