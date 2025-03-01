package com.walnut.sparta.ucdn.console.config;

import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.walnut.sparta.ucdn.console.infrastructure.SyncTransactionManage;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.ufm.FileMultiDistributionIface;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator;
import com.walnut.sparta.ucdn.console.umc.ssfm.EFileMultiDistributionIface;
import com.walnut.sparta.ucdn.console.umc.wolf.UCDNWolfRPCManage;
import com.walnut.sparta.ucdn.console.umc.wolf.WolfRPCManage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class BeanConfig {
    @Resource
    private UOFSContentDelivery uofsContentDelivery;

    @Resource
    private ServicesInstrument primaryService;

    @Resource
    private DuplexAppointClient wolfClient;

    @Bean( name = "kafkaFileServiceClient")
    public UlfBroadcastControlNode kafkaFileServiceClient(){
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", this.uofsContentDelivery, WolfMCExpress.class);
        client.compile( FileMultiDistributionIface.class,false );
        return client;
    }

    @Bean( name = "rocketFileServiceClient")
    public UlfBroadcastControlNode rocketFileServiceClient(){
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceGroup), "", this.uofsContentDelivery, WolfMCExpress.class);
        client.compile( SessionValidator.class,false );
        return client;
    }

    @Bean( name = "kafkaEFileServiceClient" )
    public UlfBroadcastControlNode kafkaEFileServiceClient() {
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", this.uofsContentDelivery, WolfMCExpress.class);
        client.compile( EFileMultiDistributionIface.class,false );
        return client;
    }

    @Bean
    public WolfRPCManage wolfRPCManage() throws Exception {
        UCDNWolfRPCManage wolfRPCManage = new UCDNWolfRPCManage(this.primaryService, this.uofsContentDelivery, this.wolfClient);
        wolfRPCManage.getLifecycleIFace().registerService( new RegisterServiceDTO( UCDNConstants.clientId, UCDNConstants.serviceId ));
        return wolfRPCManage;
    }

    @Bean
    public TransactionManage SyncTransactionManage(){
        return new SyncTransactionManage();
    }
}
