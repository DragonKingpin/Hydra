package com.pinecone.hydra.service.registry.ulf;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.hydra.service.registry.appoint.LogicServiceConnection;
import com.pinecone.hydra.umc.msg.UMCChannel;

public class HuskyServiceConnection implements LogicServiceConnection {

    protected Long                                    mClientId;

    protected final ConcurrentMap<Object, UMCChannel> mServiceChannels;

    protected SocketAddress                           mRemoteAddress;

    public HuskyServiceConnection() {
        this.mServiceChannels  = new ConcurrentHashMap<>();

    }

}
