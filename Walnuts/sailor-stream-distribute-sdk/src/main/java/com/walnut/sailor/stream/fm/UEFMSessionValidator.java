package com.walnut.sailor.stream.fm;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sailor.stream.fm.protocol.RequestHead;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;

public class UEFMSessionValidator implements SessionValidator {
    protected UlfBroadcastControlNode client;

    protected BroadcastControlProducer producer;

    protected BroadcastControlConsumer consumer;

    public UEFMSessionValidator( MasterWarehouse masterWarehouse ) throws UMBServiceException {
//        this.client = masterWarehouse.getRocketEFileClient();
//        this.producer = client.createBroadcastControlProducer();
//        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNEFileCloudDistributeTopic);
//        this.consumer.registerController( new UEFMSessionValidatorController() );
//        this.consumer.start();
//        this.producer.start();
    }

    @Override
    public void fileTransmitComplete( RequestHead head ) {
        SessionValidator sessionValidator = this.producer.getIface(SessionValidator.class, UCDNConstants.UCDNEFileCloudDistributeTopic);
        sessionValidator.fileTransmitComplete( head );
    }
}
