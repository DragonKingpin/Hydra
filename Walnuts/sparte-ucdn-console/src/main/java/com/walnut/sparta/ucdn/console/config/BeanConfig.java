package com.walnut.sparta.ucdn.console.config;

import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.walnut.sparta.ucdn.console.infrastructure.FSContentDeliveryService;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.infrastructure.UcdnConstants;
import com.walnut.sparta.ucdn.console.umc.FileDistribution;
import org.apache.kafka.clients.KafkaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class BeanConfig {
    @Resource
    UOFSContentDelivery uofsContentDelivery;

    @Bean
    public WolfMCBClient kafkaClient(){
        WolfMCBClient client = new WolfMCBClient(new WolfMCKafkaClient(UcdnConstants.KafkaServer), "", this.uofsContentDelivery, WolfMCExpress.class);
        client.compile( FileDistribution.class,false );
        return client;
    }
}
