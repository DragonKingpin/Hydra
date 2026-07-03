package com.acorn.skynet.device.husky.server;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.UMCChannel;

public class HuskyDeviceClientile implements Pinenut {

    protected long clientId = -1;

    protected final ConcurrentMap<Object, UMCChannel> channels;

    protected SocketAddress remoteAddress;

    public HuskyDeviceClientile() {
        this.channels = new ConcurrentHashMap<>();
    }

    public void attachChannel( long clientId, Object channelId, UMCChannel channel ) {
        if ( channel == null || channelId == null ) {
            return;
        }

        this.clientId = clientId;
        this.remoteAddress = channel.remoteAddress();
        this.channels.put( channelId, channel );
    }

    public void detachChannel( Object channelId ) {
        if ( channelId == null ) {
            return;
        }

        this.channels.remove( channelId );
    }

    public long getClientId() {
        return this.clientId;
    }

    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public int connectionCount() {
        return this.channels.size();
    }

    public boolean isDefunct() {
        return this.channels.isEmpty();
    }

    public Collection<UMCChannel> channels() {
        return this.channels.values();
    }
}
