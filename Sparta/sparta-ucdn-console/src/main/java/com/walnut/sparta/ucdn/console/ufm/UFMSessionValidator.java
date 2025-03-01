package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;

import java.io.IOException;

public class UFMSessionValidator implements SessionValidator {

    protected UlfBroadcastControlNode          eventMQClient;

    protected BroadcastControlProducer         eventProducer;

    protected BroadcastControlConsumer         eventConsumer;

    protected UFMConfig                        config;

    protected UOFSFileMultiDistributionService distributionService;


    public UFMSessionValidator( UOFSFileMultiDistributionService distributionService ) {
        this.config              = distributionService.config;
        this.distributionService = distributionService;
        this.eventMQClient       = distributionService.ucdnService.getPrimaryMessageMiddlewareDirector().getPrimaryRocketClient();
    }

    @Override
    public boolean hasStarted() {
        return this.eventProducer != null;
    }

    @Override
    public void start() throws UMBServiceException {
        if ( !this.hasStarted() ) {
            this.eventProducer     = this.eventMQClient.createBroadcastControlProducer();
            this.eventConsumer     = this.eventMQClient.createBroadcastControlConsumer( this.config.getFileCloudDistributeEventTopic() );
            this.eventConsumer.registerController( new UFMSessionValidatorController( this.distributionService ) );
            this.eventConsumer.start();
            this.eventProducer.start();
        }
    }

    @Override
    public void shutdown() {
        if ( this.hasStarted() ) {
            this.eventProducer.close();
            this.eventConsumer.close();
            this.eventProducer = null;
            this.eventConsumer = null;
        }
    }

    @Override
    public void stageClusterGroupComplete( String path ) throws IOException {
        this.eventProducer.issueInform(
                this.config.getFileCloudDistributeEventTopic(), "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator.stageClusterGroupComplete", path
        );
    }

    @Override
    public void stageFileTransmitComplete( String path ) throws IOException {
        this.eventProducer.issueInform(
                this.config.getFileCloudDistributeEventTopic(), "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator.stageFileTransmitComplete", path
        );
    }

    @Override
    public void fileTransmitComplete( String path, String serviceId ) throws IOException {
        this.eventProducer.issueInform(
                this.config.getFileCloudDistributeEventTopic(), "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator.fileTransmitComplete", path,serviceId
        );
    }
}
