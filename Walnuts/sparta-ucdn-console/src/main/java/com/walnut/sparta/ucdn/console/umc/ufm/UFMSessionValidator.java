package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;

import java.io.IOException;

//@Component
public class UFMSessionValidator implements SessionValidator {
//    @Resource
    private KOMFileSystem              primaryFileSystem;

    protected UlfBroadcastControlNode  client;

    protected BroadcastControlProducer producer;

    protected BroadcastControlConsumer consumer;

//    public UCDNFileDistributionSynchronize(@Qualifier("rocketFileServiceClient") UlfBroadcastControlNode client, DistributionSynchronizeController distributionSynchronizeController ) throws UMBServiceException {
//        this.producer = client.createBroadcastControlProducer();
//        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic);
//        this.consumer.registerController( distributionSynchronizeController );
//        this.consumer.start();
//        this.producer.start();
//        this.client = client;
//    }

    public UFMSessionValidator( MasterWarehouse masterWarehouse ) throws UMBServiceException {
        this.primaryFileSystem = masterWarehouse.getKOMFileSystem();
        this.client = masterWarehouse.getRocketClient();
        this.producer = client.createBroadcastControlProducer();
        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic);
        this.consumer.registerController( new UFMSessionValidatorController( masterWarehouse ) );
        this.consumer.start();
        this.producer.start();
    }

    @Override
    public void stageClusterGroupComplete( String path ) throws IOException {
        this.producer.issueInform(
                UCDNConstants.UCDNFileCloudDistributeTopic, "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator.stageClusterGroupComplete", path
        );
    }

    @Override
    public void stageFileTransmitComplete( String path ) throws IOException {
        this.producer.issueInform(
                UCDNConstants.UCDNFileCloudDistributeTopic, "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator.stageFileTransmitComplete", path
        );
    }
}
