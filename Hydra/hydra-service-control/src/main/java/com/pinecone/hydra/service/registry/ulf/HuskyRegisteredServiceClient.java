package com.pinecone.hydra.service.registry.ulf;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.hydra.service.registry.appoint.RegisteredServiceClient;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.umc.msg.UMCChannel;

public class HuskyRegisteredServiceClient implements RegisteredServiceClient {

    protected long                                    mClientId = -1;

    protected final ConcurrentMap<Object, UMCChannel> mServiceChannels;

    protected final ServiceAppointServer              mServiceAppointServer;

    protected SocketAddress                           mRemoteAddress;

    public HuskyRegisteredServiceClient( ServiceAppointServer serviceAppointServer ) {
        this.mServiceChannels      = new ConcurrentHashMap<>();
        this.mServiceAppointServer = serviceAppointServer;
    }

    @Override
    public long getClientId() {
        return this.mClientId;
    }

    @Override
    public int connectionCount() {
        return this.mServiceChannels.size();
    }

    @Override
    public boolean isDefunct() {
        return this.mServiceChannels.isEmpty();
    }

    @Override
    public Object queryNativeConnection( Object connectionIdentity ) {
        return this.mServiceChannels.get( connectionIdentity );
    }

    @Override
    public Collection<?> connections() {
        return this.mServiceChannels.values();
    }

    @Override
    public void shutdown() {
        for ( UMCChannel umcChannel : this.mServiceChannels.values() ) {
            umcChannel.close();
        }
        this.mServiceChannels.clear();
    }

    @Override
    public ServiceAppointServer serviceAppointServer() {
        return this.mServiceAppointServer;
    }

    @Override
    public void afterNewConnectionInbound( Long clientId, Object connectId, Object connection, Object context ) {
        UMCChannel channel = (UMCChannel) connection;
        this.mServiceChannels.put( connectId, channel );
        this.mClientId     = clientId;
    }

    @Override
    public void afterConnectionDetach( Long clientId, Object channelId, Object connection ) {
        this.mServiceChannels.remove( channelId );
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return this.mRemoteAddress;
    }

}
