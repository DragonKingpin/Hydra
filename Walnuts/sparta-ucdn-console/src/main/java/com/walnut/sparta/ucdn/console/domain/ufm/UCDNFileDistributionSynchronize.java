package com.walnut.sparta.ucdn.console.domain.ufm;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sparta.ucdn.console.domain.ufm.FileDistributionSynchronize;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.DistributionSynchronizeController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class UCDNFileDistributionSynchronize implements FileDistributionSynchronize {
    @Resource
    private KOMFileSystem              primaryFileSystem;

    protected UlfBroadcastControlNode  client;

    protected BroadcastControlProducer producer;

    protected BroadcastControlConsumer consumer;

    public UCDNFileDistributionSynchronize(@Qualifier("rocketFileServiceClient") UlfBroadcastControlNode client, DistributionSynchronizeController distributionSynchronizeController ) throws UMBServiceException {
        this.producer = client.createBroadcastControlProducer();
        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic);
        this.consumer.registerController( distributionSynchronizeController );
        this.consumer.start();
        this.producer.start();
        this.client = client;
    }

    public void distributionCallBack( String path ) throws IOException {
        this.producer.issueInform( UCDNConstants.UCDNFileCloudDistributeTopic, "com.walnut.sparta.ucdn.console.umc.DistributionSynchronize.distributionCallBack", path );
    }
}
