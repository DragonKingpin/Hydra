package com.acorn.redqueen.service.conduct;

import org.slf4j.Logger;

import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.system.Hydrogen;

public class RedCollectiveServiceRegiment implements CollectiveServiceRegiment {

    protected ServiceManager     mServiceManager;

    protected ServiceInstrument  mServiceInstrument;

    protected Hydrogen           mSystem;

    protected Logger             mLogger;

    public RedCollectiveServiceRegiment(  ) {

    }

    @Override
    public Logger getLogger() {
        return null;
    }
}
