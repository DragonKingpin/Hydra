package com.walnut.sparta.ucdn.console.config;

import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.FileDistribution;
import com.walnut.sparta.ucdn.console.umc.DistributionSynchronize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class BeanConfig {
    @Resource
    UOFSContentDelivery uofsContentDelivery;

    @Bean( name = "kafkaFileServiceClient")
    public UlfBroadcastControlNode kafkaFileServiceClient(){
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", this.uofsContentDelivery, WolfMCExpress.class);
        client.compile( FileDistribution.class,false );
        return client;
    }

    @Bean( name = "rocketFileServiceClient")
    public UlfBroadcastControlNode rocketFileServiceClient(){
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceGroup), "", this.uofsContentDelivery, WolfMCExpress.class);
        client.compile( DistributionSynchronize.class,false );
        return client;
    }
}
