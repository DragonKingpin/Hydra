package com.walnut.sparta.ucdn.console.umc.ufmc;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.umc.ufm.UFMSessionValidatorController;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

public class UEFMSessionValidator implements ExternalSessionValidator{
    protected UlfBroadcastControlNode client;

    protected BroadcastControlProducer producer;

    protected BroadcastControlConsumer consumer;

    public UEFMSessionValidator( MasterWarehouse masterWarehouse ) throws UMBServiceException {
        this.client = masterWarehouse.getRocketEFileClient();
        this.producer = client.createBroadcastControlProducer();
        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNEFileCloudDistributeTopic);
        this.consumer.registerController( new UEFMSessionValidatorController() );
        this.consumer.start();
        this.producer.start();
    }

    @Override
    public void fileTransmitComplete(RequestHead head) {
        ExternalSessionValidator sessionValidator = this.producer.getIface(ExternalSessionValidator.class, UCDNConstants.UCDNEFileCloudDistributeTopic);
        sessionValidator.fileTransmitComplete( head );
    }
}
