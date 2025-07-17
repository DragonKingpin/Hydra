package com.acorn.redqueen.service.conduct;

import com.acorn.redqueen.service.RedQueenServiceControllerException;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceControlException;
import com.pinecone.hydra.service.registry.ServiceManager;
import com.pinecone.hydra.service.registry.client.ServiceManagerClient;
import com.pinecone.hydra.system.Hydrogen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedCollectiveServiceRegiment implements CollectiveServiceRegiment {

    protected ServiceManager                    mServiceManager;

    protected ServiceInstrument                 mServiceInstrument;

    protected Hydrogen                          mSystem;

    protected Logger                            mLogger;


    public RedCollectiveServiceRegiment(
            Hydrogen system, ServiceInstrument serviceInstrument,
            ServiceManager serviceManager
    ) {
        this.mSystem                    = system;
        this.mServiceInstrument         = serviceInstrument;
        this.mLogger                    = LoggerFactory.getLogger( "RedCollectiveServiceRegiment" );
        this.mServiceManager            = serviceManager;
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public void startServiceManage() throws ServiceControlException {
        this.mServiceManager.startService();

        this.mLogger.info( "RPC init success" );
    }

    @Override
    public ServiceManager serviceManager() {
        return this.mServiceManager;
    }


    @Override
    public ServiceInstrument serviceInstrument() {
        return this.mServiceInstrument;
    }
}
