package com.walnut.sparta.ucdn.console.config;

import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNContentDelivery;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.ufm.FileMultiDistributionIface;
import com.walnut.sparta.ucdn.console.ufm.SessionValidator;
import com.walnut.sparta.ucdn.console.infrastructure.service.UCDNCentralServiceManager;
import com.walnut.sparta.ucdn.console.infrastructure.service.UCDNServiceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class BeanConfig {
    @Resource
    private UCDNContentDelivery UCDNContentDelivery;

    @Resource
    private ServiceInstrument primaryService;

    @Bean( name = "kafkaFileServiceClient")
    public UlfBroadcastControlNode kafkaFileServiceClient(){
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", this.UCDNContentDelivery, WolfMCExpress.class);
        client.compile( FileMultiDistributionIface.class,false );
        return client;
    }

    @Bean( name = "rocketFileServiceClient")
    public UlfBroadcastControlNode rocketFileServiceClient(){
        UlfBroadcastControlNode client = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceTransmitGroup), "", this.UCDNContentDelivery, WolfMCExpress.class);
        client.compile( SessionValidator.class,false );
        return client;
    }

    @Bean
    public UCDNServiceManager ucdnServiceManager() throws Exception {
        UCDNCentralServiceManager ucdnServiceManager = new UCDNCentralServiceManager(this.UCDNContentDelivery);
        ucdnServiceManager.getLifecycleIface().registerService( new RegisterServiceDTO( UCDNConstants.clientId, UCDNConstants.serviceId ));
        return ucdnServiceManager;
    }
}
