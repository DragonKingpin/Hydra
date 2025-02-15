package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.walnut.sparta.ucdn.console.umc.ufm.FileMultiDistributionIface;
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

    public UMCMasterWarehouse(KOMFileSystem fileSystem, UniformVolumeManager primaryVolume, UOFSContentDelivery uofsContentDelivery){
        this.primaryFileSystem = fileSystem;
        this.primaryVolume = primaryVolume;
        this.sessionPhaser = new UFMSessionPhaser();

        UlfBroadcastControlNode kafka = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", uofsContentDelivery, WolfMCExpress.class);
        kafka.compile( FileMultiDistributionIface.class,false );
        this.kafkaClient = kafka;

        UlfBroadcastControlNode rocket = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceGroup), "", uofsContentDelivery, WolfMCExpress.class);
        rocket.compile( SessionValidator.class,false );
        this.rocketClient = rocket;
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
    public UlfBroadcastControlNode getRocketClient() {
        return this.rocketClient;
    }
}
