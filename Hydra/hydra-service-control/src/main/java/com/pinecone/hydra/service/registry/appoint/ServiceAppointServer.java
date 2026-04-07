package com.pinecone.hydra.service.registry.appoint;

import com.pinecone.hydra.appoints.AppointNodus;
import com.pinecone.hydra.service.registry.server.ServiceManager;

public interface ServiceAppointServer extends AppointNodus {

    ServiceAppointServer hookServiceManager( ServiceManager serviceManager );

    ServiceManager serviceManager();

    boolean isTerminated();

    boolean isStarted();

}
