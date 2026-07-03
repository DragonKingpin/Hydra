package com.pinecone.hydra.device.registry.server.detached;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceDetachedObservationRegistry implements Pinenut {

    protected final ConcurrentMap<Long, DeviceDetachedObservationEntry> clientEntries;

    protected final ConcurrentMap<GUID, Long> instanceClientIndex;

    public DeviceDetachedObservationRegistry() {
        this.clientEntries = new ConcurrentHashMap<>();
        this.instanceClientIndex = new ConcurrentHashMap<>();
    }

    public void put( DeviceDetachedObservationEntry entry ) {
        if ( entry == null || entry.getClientId() == null || entry.getInstanceGuid() == null ) {
            return;
        }
        DeviceDetachedObservationEntry previous = this.clientEntries.put( entry.getClientId(), entry );
        if ( previous != null && previous.getInstanceGuid() != null ) {
            this.instanceClientIndex.remove( previous.getInstanceGuid() );
        }
        this.instanceClientIndex.put( entry.getInstanceGuid(), entry.getClientId() );
    }

    public DeviceDetachedObservationEntry get( Long clientId ) {
        if ( clientId == null ) {
            return null;
        }
        return this.clientEntries.get( clientId );
    }

    public DeviceDetachedObservationEntry getByInstanceGuid( GUID instanceGuid ) {
        if ( instanceGuid == null ) {
            return null;
        }
        Long clientId = this.instanceClientIndex.get( instanceGuid );
        return clientId == null ? null : this.get( clientId );
    }

    public DeviceDetachedObservationEntry remove( Long clientId ) {
        if ( clientId == null ) {
            return null;
        }
        DeviceDetachedObservationEntry removed = this.clientEntries.remove( clientId );
        if ( removed != null && removed.getInstanceGuid() != null ) {
            this.instanceClientIndex.remove( removed.getInstanceGuid() );
        }
        return removed;
    }

    public DeviceDetachedObservationEntry removeByInstanceGuid( GUID instanceGuid ) {
        if ( instanceGuid == null ) {
            return null;
        }
        Long clientId = this.instanceClientIndex.remove( instanceGuid );
        return clientId == null ? null : this.clientEntries.remove( clientId );
    }

    public Collection<DeviceDetachedObservationEntry> snapshotExpired( long nowMillis ) {
        ArrayList<DeviceDetachedObservationEntry> expired = new ArrayList<>();
        for ( DeviceDetachedObservationEntry entry : this.clientEntries.values() ) {
            if ( entry != null && entry.getDeadlineMillis() <= nowMillis ) {
                expired.add( entry );
            }
        }
        return expired;
    }
}
