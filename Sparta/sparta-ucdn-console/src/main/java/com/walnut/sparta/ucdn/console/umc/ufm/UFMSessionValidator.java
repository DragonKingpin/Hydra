package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sparta.ucdn.console.infrastructure.SpartaUCDNService;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNService;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;

import java.io.IOException;

//@Component
public class UFMSessionValidator implements SessionValidator {
//    @Resource
    private KOMFileSystem              primaryFileSystem;

    protected UlfBroadcastControlNode  client;

    protected BroadcastControlProducer producer;

    protected BroadcastControlConsumer consumer;


    public UFMSessionValidator(MasterWarehouse masterWarehouse, UCDNService ucdnService) throws UMBServiceException {
        this.primaryFileSystem = ucdnService.getKOMFileSystem();
        this.client = ucdnService.getRocketClient();
        this.producer = client.createBroadcastControlProducer();
        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic);
        this.consumer.registerController( new UFMSessionValidatorController( masterWarehouse, ucdnService ) );
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

    @Override
    public void fileTransmitComplete(String path, String serviceId) throws IOException {
        this.producer.issueInform(
                UCDNConstants.UCDNFileCloudDistributeTopic, "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator.fileTransmitComplete", path,serviceId
        );
    }
}
