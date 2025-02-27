package com.pinecone.hydra.uma.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.FairChannelPool;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.MultiClientChannelRegistry;
import com.pinecone.hydra.umc.wolf.UlfIOLoadBalanceStrategy;
import com.pinecone.hydra.umc.wolf.UlfIdleFirstBalanceStrategy;
import com.pinecone.hydra.umc.wolf.client.ProactiveParallelFairChannelPool;

public class GenericMultiClientChannelRegistry<CID > implements MultiClientChannelRegistry<CID > {
    protected static final UlfIOLoadBalanceStrategy LoadBalanceStrategy = new UlfIdleFirstBalanceStrategy();

    protected Lock                          mPoolLock;

    protected Map<CID, FairChannelPool >    mClientChannelRegistry;

    public GenericMultiClientChannelRegistry() {
        this.mClientChannelRegistry = new ConcurrentHashMap<>();
        this.mPoolLock              = new ReentrantLock();
    }

    @Override
    public void register( CID id, ChannelControlBlock controlBlock ) {
        FairChannelPool pool = this.mClientChannelRegistry.computeIfAbsent( id, (k)->{
            return new ProactiveParallelFairChannelPool<>( LoadBalanceStrategy );
        } );
        pool.add( controlBlock );
    }

    @Override
    public void deregister( CID id, ChannelControlBlock controlBlock ) {
        FairChannelPool pool = this.mClientChannelRegistry.computeIfPresent( id, (k, v)->{
            v.remove( controlBlock );
            if ( v.isEmpty() ) {
                MessageNode messageNode = controlBlock.getParentMessageNode();
                if ( messageNode instanceof Slf4jTraceable) {
                    ((Slf4jTraceable) messageNode).getLogger().info( "Client `{}` is detached.", id );
                }
                return null;
            }

            return v;
        } );
    }

    @Override
    public void deregister( CID id ) {
        FairChannelPool pool = this.mClientChannelRegistry.remove( id );
        if ( pool != null ) {
            pool.clear(); // All channels should be closed in this method, in principle.
        }
    }

    @Override
    public ChannelPool getPool( CID id ) {
        return this.mClientChannelRegistry.get( id );
    }

    @Override
    public int size() {
        return this.mClientChannelRegistry.size();
    }

    @Override
    public void clear() {
        this.mPoolLock.lock();
        try{
            for( FairChannelPool pool : this.mClientChannelRegistry.values() ) {
                pool.clear(); // All channels should be closed in this method, in principle.
            }
            this.mClientChannelRegistry.clear();
        }
        finally {
            this.mPoolLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return this.mClientChannelRegistry.isEmpty();
    }
}
