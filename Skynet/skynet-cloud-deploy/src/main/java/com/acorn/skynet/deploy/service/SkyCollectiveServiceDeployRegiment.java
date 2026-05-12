package com.acorn.skynet.deploy.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEvent;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEventHandler;
import org.slf4j.Logger;

public class SkyCollectiveServiceDeployRegiment implements CollectiveServiceDeployRegiment {

    protected Logger                mLogger;

    protected ServiceManager        mServiceManager;

    protected DeviceInstrument      mDeviceInstrument;


    public SkyCollectiveServiceDeployRegiment( ServiceManager serviceManager, DeviceInstrument deviceInstrument ) {
        this.mServiceManager        = serviceManager;
        this.mDeviceInstrument      = deviceInstrument;

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
