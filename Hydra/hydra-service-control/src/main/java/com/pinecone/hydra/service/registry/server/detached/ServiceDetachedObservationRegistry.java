package com.pinecone.hydra.service.registry.server.detached;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceDetachedObservationRegistry implements Pinenut {

    protected final ConcurrentMap<Long, ServiceDetachedObservationEntry> entries;

    public ServiceDetachedObservationRegistry() {
        this.entries = new ConcurrentHashMap<>();
    }

    public void put( ServiceDetachedObservationEntry entry ) {
        if ( entry == null || entry.getClientId() == null ) {
            return;
        }
        this.entries.put( entry.getClientId(), entry );
    }

    public ServiceDetachedObservationEntry get( Long clientId ) {
        if ( clientId == null ) {
            return null;
        }
        return this.entries.get( clientId );
    }

    public ServiceDetachedObservationEntry remove( Long clientId ) {
        if ( clientId == null ) {
            return null;
        }
        return this.entries.remove( clientId );
    }

    public Collection<ServiceDetachedObservationEntry> snapshotExpired( long nowMillis ) {
        ArrayList<ServiceDetachedObservationEntry> expired = new ArrayList<>();
        for ( ServiceDetachedObservationEntry entry : this.entries.values() ) {
            if ( entry != null && entry.getDeadlineMillis() <= nowMillis ) {
                expired.add( entry );
            }
        }
        return expired;
    }
}
