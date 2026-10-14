package com.pinecone.hydra.service.registry.server.reconciliation;

import java.util.Objects;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;

public class ServiceInstanceObservation implements Pinenut {

    protected final ServiceInstanceEntry registration;
    protected final long firstMissingNanos;
    protected boolean settled;

    public ServiceInstanceObservation( ServiceInstanceEntry registration, long firstMissingNanos ) {
        GenericServiceInstanceEntity snapshot = new GenericServiceInstanceEntity();
        snapshot.setGuid( registration.getGuid() );
        snapshot.setServiceGuid( registration.getServiceGuid() );
        snapshot.setClientId( registration.getClientId() );
        snapshot.setRegisterTime( registration.getRegisterTime() );
        snapshot.setLatestStartTime( registration.getLatestStartTime() );
        snapshot.setLastHeartbeatTime( registration.getLastHeartbeatTime() );
        snapshot.setConnectionCount( registration.getConnectionCount() );
        this.registration = snapshot;
        this.firstMissingNanos = firstMissingNanos;
    }

    public long getFirstMissingNanos() {
        return this.firstMissingNanos;
    }

    public boolean isSettled() {
        return this.settled;
    }

    public void markSettled() {
        this.settled = true;
    }

    public boolean matchesRegistration( ServiceInstanceEntry current ) {
        return current != null
                && Objects.equals( this.registration.getGuid(), current.getGuid() )
                && Objects.equals( this.registration.getServiceGuid(), current.getServiceGuid() )
                && Objects.equals( this.registration.getClientId(), current.getClientId() )
                && Objects.equals( this.registration.getRegisterTime(), current.getRegisterTime() )
                && Objects.equals( this.registration.getLatestStartTime(), current.getLatestStartTime() )
                && Objects.equals( this.registration.getLastHeartbeatTime(), current.getLastHeartbeatTime() )
                && this.registration.getConnectionCount() == current.getConnectionCount();
    }
}
