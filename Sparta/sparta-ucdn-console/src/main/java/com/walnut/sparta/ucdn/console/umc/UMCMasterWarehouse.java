package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.walnut.sparta.ucdn.console.domain.service.WebSocketService;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.umc.ssfm.*;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionPhaser;
import com.walnut.sparta.ucdn.console.umc.ufm.UFMSessionPhaser;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;

public class UMCMasterWarehouse implements MasterWarehouse{
    private SessionPhaser               sessionPhaser;

    private KOMFileSystem               primaryFileSystem;

    private UniformVolumeManager        primaryVolume;

    private UlfBroadcastControlNode     kafkaClient;

    private UlfBroadcastControlNode     rocketClient;

    private UlfBroadcastControlNode     kafkaEFileClient;

    private UlfBroadcastControlNode     rocketEFileClient;

    private ExternalSessionPhaser       externalSessionPhaser;

    private VersionManage               versionManage;

    private TransactionManage           transactionManage;

    private WebSocketService            webSocketService;

    public UMCMasterWarehouse(KOMFileSystem fileSystem, UniformVolumeManager primaryVolume, UOFSContentDelivery uofsContentDelivery,
                              VersionManage versionManage, TransactionManage transactionManage,WebSocketService webSocketService){
        this.primaryFileSystem = fileSystem;
        this.primaryVolume = primaryVolume;
        this.sessionPhaser = new UFMSessionPhaser();
        this.externalSessionPhaser = new UFMCSessionPhaser();
        this.versionManage = versionManage;
        this.transactionManage = transactionManage;
        this.webSocketService = webSocketService;

//        UlfBroadcastControlNode kafka = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", uofsContentDelivery, WolfMCExpress.class);
//        kafka.compile( FileMultiDistributionIface.class,false );
//        this.kafkaClient = kafka;

        UlfBroadcastControlNode rocket = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceGroup), "", uofsContentDelivery, WolfMCExpress.class);
        rocket.compile( SessionValidator.class,false );
        this.rocketClient = rocket;

        UlfBroadcastControlNode rocketEFileClient = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceGroup), "", uofsContentDelivery, WolfMCExpress.class);
        rocketEFileClient.compile( ExternalSessionValidator.class,false );
        this.rocketEFileClient = rocketEFileClient;

        UlfBroadcastControlNode kafkaEFileClient = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", uofsContentDelivery, WolfMCExpress.class);
        kafkaEFileClient.compile( EFileMultiDistributionIface.class,false );
        //kafkaEFileClient.compile( JarDistributionIface.class,false );
        this.kafkaEFileClient = kafkaEFileClient;
        this.kafkaClient = kafkaEFileClient;

    }


    @Override
    public SessionPhaser getSessionPhaser() {
        return this.sessionPhaser;
    }

    @Override
    public KOMFileSystem getKOMFileSystem() {
        return this.primaryFileSystem;
    }

    @Override
    public UniformVolumeManager getUniformVolumeManager() {
        return this.primaryVolume;
    }

    @Override
    public UlfBroadcastControlNode getKafkaClient() {
        return this.kafkaClient;
    }

    @Override
    public UlfBroadcastControlNode getKafkaEFileClient() {
        return this.kafkaEFileClient;
    }

    @Override
    public UlfBroadcastControlNode getRocketClient() {
        return this.rocketClient;
    }

    @Override
    public UlfBroadcastControlNode getRocketEFileClient() {
        return this.rocketEFileClient;
    }

    @Override
    public ExternalSessionPhaser getExternalSessionPhaser() {
        return this.externalSessionPhaser;
    }

    @Override
    public VersionManage getVersionManage() {
        return this.versionManage;
    }

    @Override
    public TransactionManage getTransactionManage() {
        return this.transactionManage;
    }

    @Override
    public WebSocketService getWebSocketService() {
        return this.webSocketService;
    }
}
