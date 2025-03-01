package com.walnut.sailor.stream.fm;

import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

public class SFMSessionValidator implements SessionValidator {

    protected BroadcastControlProducer producer;

    protected SessionValidator sessionValidator;

    protected SingleStreamFileMultiDistributionService distributionService;

    public SFMSessionValidator( SingleStreamFileMultiDistributionService service ) {
        this.producer            = service.getTransmitProducer();
        this.distributionService = service;
        this.sessionValidator    = this.producer.getIface( SessionValidator.class, this.distributionService.getConfig().getFileCloudDistributeTransmitTopic() );
    }

    @Override
    public void fileTransmitComplete( RequestHead head ) {
        this.sessionValidator.fileTransmitComplete( head );
    }

}
