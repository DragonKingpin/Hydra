package com.acorn.skynet.device.grpc.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcDeviceLifecycleClientile implements Pinenut {

    protected final long clientId;

    protected final Map<String, GrpcDeviceLifecycleSession> sessions;

    public GrpcDeviceLifecycleClientile( long clientId ) {
        this.clientId = clientId;
        this.sessions = new ConcurrentHashMap<>();
    }

    public long clientId() {
        return this.clientId;
    }

    public void attachSession( GrpcDeviceLifecycleSession session ) {
        if ( session != null ) {
            this.sessions.put( session.sessionGuid(), session );
        }
    }

    public void detachSession( GrpcDeviceLifecycleSession session ) {
        if ( session != null ) {
            this.sessions.remove( session.sessionGuid(), session );
        }
    }

    public boolean isActive() {
        return this.activeSession() != null;
    }

    public GrpcDeviceLifecycleSession activeSession() {
        for ( GrpcDeviceLifecycleSession session : this.sessions.values() ) {
            if ( session != null && session.isActive() ) {
                return session;
            }
        }
        return null;
    }

    public Collection<GrpcDeviceLifecycleSession> sessions() {
        return new ArrayList<>( this.sessions.values() );
    }
}
