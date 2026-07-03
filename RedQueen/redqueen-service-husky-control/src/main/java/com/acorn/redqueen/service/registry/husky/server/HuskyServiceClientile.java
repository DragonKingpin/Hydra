package com.acorn.redqueen.service.registry.husky.server;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.service.registry.appoint.ServiceClientile;
import com.pinecone.hydra.umc.msg.UMCChannel;

public class HuskyServiceClientile implements ServiceClientile {

    protected long                                    mnClientId = -1;

    protected final ConcurrentMap<Object, UMCChannel> mServiceChannels;

    protected final HuskyServiceControlTransport      mServiceControlTransport;

    protected SocketAddress                           mMainRemoteAddress;

    public HuskyServiceClientile( HuskyServiceControlTransport serviceControlTransport ) {
        this.mServiceChannels         = new ConcurrentHashMap<>();
        this.mServiceControlTransport = serviceControlTransport;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return this.mMainRemoteAddress;
    }

    @Override
    public void afterNewConnectionInbound( Long clientId, Object connectId, Object connection, Object context ) {
        UMCChannel channel = (UMCChannel) connection;
        this.mServiceChannels.put( connectId, channel );
        this.mnClientId = clientId;
        this.mMainRemoteAddress = channel.remoteAddress();
    }

    @Override
    public void afterConnectionDetach( Long clientId, Object channelId, Object connection ) {
        this.mServiceChannels.remove( channelId );
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
        for ( UMCChannel channel : this.mServiceChannels.values() ) {
            channel.close();
        }
        this.mServiceChannels.clear();
    }

    public HuskyServiceControlTransport serviceControlTransport() {
        return this.mServiceControlTransport;
    }

}

