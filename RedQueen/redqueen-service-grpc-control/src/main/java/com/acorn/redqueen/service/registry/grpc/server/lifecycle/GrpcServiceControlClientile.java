package com.acorn.redqueen.service.registry.grpc.server.lifecycle;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlSession;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.service.registry.appoint.ServiceClientile;

public class GrpcServiceControlClientile implements ServiceClientile, Pinenut {

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

    @Override
    public SocketAddress getRemoteAddress() {
        GrpcServiceControlSession session = this.activeSession();
        if ( session == null ) {
            return InetSocketAddress.createUnresolved( "0.0.0.0", 0 );
        }
        return this.toSocketAddress( session.remoteAddress() );
    }

    @Override
    public void afterNewConnectionInbound( Long clientId, Object connectId, Object connection, Object context ) {
        if ( connection instanceof GrpcServiceControlSession ) {
            this.attachSession( (GrpcServiceControlSession) connection );
        }
    }

    @Override
    public void afterConnectionDetach( Long clientId, Object channelId, Object connection ) {
        if ( connection instanceof GrpcServiceControlSession ) {
            this.detachSession( (GrpcServiceControlSession) connection );
        }
    }

    @Override
    public ServiceAppointServer serviceAppointServer() {
        return null;
    }

    @Override
    public long getClientId() {
        return this.mnClientId;
    }

    @Override
    public int connectionCount() {
        return this.mSessionMap.size();
    }

    @Override
    public boolean isDefunct() {
        return !this.isActive();
    }

    @Override
    public Object queryNativeConnection( Object connectionIdentity ) {
        if ( connectionIdentity == null ) {
            return null;
        }
        return this.mSessionMap.get( String.valueOf( connectionIdentity ) );
    }

    @Override
    public Collection<?> connections() {
        return this.sessions();
    }

    @Override
    public void shutdown() {
        for ( GrpcServiceControlSession session : this.mSessionMap.values() ) {
            session.close();
        }
        this.mSessionMap.clear();
    }

    protected SocketAddress toSocketAddress( String szRemoteAddress ) {
        if ( szRemoteAddress == null || szRemoteAddress.isBlank() ) {
            return InetSocketAddress.createUnresolved( "0.0.0.0", 0 );
        }

        String szValue = szRemoteAddress.trim();
        if ( szValue.startsWith( "/" ) ) {
            szValue = szValue.substring( 1 );
        }
        int nIndex = szValue.lastIndexOf( ':' );
        if ( nIndex < 1 || nIndex >= szValue.length() - 1 ) {
            return InetSocketAddress.createUnresolved( szValue, 0 );
        }

        String szHost = szValue.substring( 0, nIndex );
        int nPort = Integer.parseInt( szValue.substring( nIndex + 1 ) );
        return InetSocketAddress.createUnresolved( szHost, nPort );
    }
}




