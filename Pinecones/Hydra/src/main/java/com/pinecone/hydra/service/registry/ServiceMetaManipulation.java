package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolfmc.server.WolfMCServer;
import com.pinecone.hydra.umct.ServiceException;

import java.util.Collection;

public class ServiceMetaManipulation implements ServiceMetaManipulationIface{
    private WolvesAppointServer             nWolf;

    private ServiceMetaManipulationIface    serviceMetaManipulationIface;

    public ServiceMetaManipulation(WolfMCServer server, USII usii, ServiceManager serviceManager ) throws ServiceException {
        this.nWolf = new WolvesAppointServer(server, HuskyDuplexExpress.class);
        this.nWolf.registerController( new ServiceLifecycleController( serviceManager ) );
        nWolf.execute();
        nWolf.compile( ServiceLifecycleIface.class, false );
        this.serviceMetaManipulationIface = this.nWolf.getIface( usii.getClientId(), ServiceMetaManipulationIface.class );
    }

    @Override
    public Collection<ServiceInstance> queryServiceInstanceByClientId(Long clientId) {
        return this.serviceMetaManipulationIface.queryServiceInstanceByClientId( clientId );
    }

    @Override
    public Collection<ServiceInstance> queryServiceInstanceByServiceId(Identification serviceId) {
        return this.serviceMetaManipulationIface.queryServiceInstanceByServiceId( serviceId );
    }

    @Override
    public Collection<ServiceInstance> queryServiceInstanceByUSII(USII usii) {
        return this.serviceMetaManipulationIface.queryServiceInstanceByUSII( usii );
    }
}
