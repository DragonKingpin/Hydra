package com.walnut.odin.conduct;

import com.pinecone.hydra.uma.DuplexAppointServer;

public class RavenProcessorDeployManager implements ProcessorDeployManager {

    protected CollectiveTaskRegiment    mCollectiveTaskRegiment;

    protected DuplexAppointServer       mDuplexAppointServer;

    public RavenProcessorDeployManager( CollectiveTaskRegiment regiment ) {
        this.mCollectiveTaskRegiment = regiment;
        this.mDuplexAppointServer = this.mCollectiveTaskRegiment.remoteProcessManagerServer().duplexAppointServer();
    }

}
