package com.acorn.skynet.deploy.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEvent;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEventHandler;
import org.slf4j.Logger;

public class SkyCollectiveServiceDeployRegiment implements CollectiveServiceDeployRegiment {

    protected Logger                mLogger;

    protected ServiceManager        mServiceManager;

    protected DeployInstrument      mDeployInstrument;


    public SkyCollectiveServiceDeployRegiment( ServiceManager serviceManager, DeployInstrument deployInstrument ) {
        this.mServiceManager        = serviceManager;
        this.mDeployInstrument      = deployInstrument;

        this.initServiceEvent();
    }

    protected void initServiceEvent() {
        this.mServiceManager.addRegisterEventHandler(new ServiceRegisterEventHandler() {
            @Override
            public void fired( long clientId, GUID insId, GUID serviceId, ServiceRegisterEvent event, Object caused ) {

            }
        });
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }
}
