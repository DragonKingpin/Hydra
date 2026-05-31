package com.acorn.redqueen.service.registry.grpc.server.lifecycle;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlSession;
import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcServiceControlClientile implements Pinenut {

    protected long mnClientId;

    protected Map<String, GrpcServiceControlSession> mSessionMap;

    public GrpcServiceControlClientile( long nClientId ) {
        this.mnClientId = nClientId;
        this.mSessionMap = new ConcurrentHashMap<>();
    }

    public long clientId() {
        return this.mnClientId;
    }

    public void attachSession( GrpcServiceControlSession session ) {
        this.mSessionMap.put( session.sessionGuid(), session );
    }

    public void detachSession( GrpcServiceControlSession session ) {
        if ( session == null ) {
            return;
        }
        this.mSessionMap.remove( session.sessionGuid() );
    }

    public Collection<GrpcServiceControlSession> sessions() {
        return Collections.unmodifiableCollection( this.mSessionMap.values() );
    }

    public GrpcServiceControlSession activeSession() {
        for ( GrpcServiceControlSession session : this.mSessionMap.values() ) {
            if ( session.isActive() ) {
                return session;
            }
        }
        return null;
    }

    public boolean isActive() {
        for ( GrpcServiceControlSession session : this.mSessionMap.values() ) {
            if ( session.isActive() ) {
                return true;
            }
        }
        return false;
    }
}




