package com.pinecone.hydra.umc.wolf.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.EventLoop;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.umc.msg.AsyncMessenger;
import com.pinecone.hydra.umc.msg.ChannelAllocateException;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.MediumTerminationException;
import com.pinecone.hydra.umc.msg.Messenger;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.umc.wolf.UlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.wolf.UlfIdleFirstBalanceStrategy;
import com.pinecone.hydra.umc.wolf.UlfMessageNode;
import com.pinecone.hydra.umc.wolf.WolfMCNode;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ArchAsyncMessenger extends WolfMCNode implements AsyncMessenger, UlfMessageNode {
    protected final ReentrantLock                                  mSynRequestLock  = new ReentrantLock();
    protected ProactiveParallelFairSyncChannelPool<ChannelId >     mChannelPool     ;
    //protected BlockingDeque<UMCMessage>                            mSyncRetMsgQueue = new LinkedBlockingDeque<>();

    public ArchAsyncMessenger( long nodeId, String szName, Processum parentProcess, UlfMessageNode parent, Map<String, Object> joConf, ExtraHeadCoder extraHeadCoder ) {
        super( nodeId, szName, parentProcess, parent, joConf, extraHeadCoder );

        this.mChannelPool   = new ProactiveParallelFairSyncChannelPool<>( this.mSynRequestLock, new UlfIdleFirstBalanceStrategy() ); //TODO
        //this.makeNameAndId();
    }

    public ArchAsyncMessenger(long nodeId, String szName, Hydrogen system, Map<String, Object> joConf, ExtraHeadCoder extraHeadCoder ) {
        this( nodeId, szName, system, null, joConf, extraHeadCoder );
    }


    @Override
    public ProactiveParallelFairSyncChannelPool   getChannelPool() {
        return this.mChannelPool;
    }

    Lock                                      getSynRequestLock() {
        return this.mSynRequestLock;
    }

    protected long getSyncWaitingMillis() {
        return ArchAsyncMessenger.getSyncWaitingMillis( this );
    }

    UlfAsyncMessengerChannelControlBlock      nextSynChannelCB() throws IOException {
        UlfAsyncMessengerChannelControlBlock block = (UlfAsyncMessengerChannelControlBlock) this.getChannelPool().nextSyncChannel( this.getChannelPool().getMajorWaitTimeout() * 2 );
        if( block == null ) {
            throw new ChannelAllocateException( "Channel allocate failed." );
        }
        reconnect( block, this.getSyncWaitingMillis() );
        return block;
    }

    UlfAsyncMessengerChannelControlBlock      nextAsyChannelCB() throws IOException  {
        UlfAsyncMessengerChannelControlBlock block = (UlfAsyncMessengerChannelControlBlock) this.getChannelPool().nextAsynChannel( this.getChannelPool().getMajorWaitTimeout() * 2 );
        if( block == null ) {
            throw new ChannelAllocateException( "Channel allocate failed." );
        }
        reconnect( block, this.getSyncWaitingMillis() );
        return block;
    }

//    BlockingDeque<UMCMessage >                getSyncRetMsgQueue() {
//        return this.mSyncRetMsgQueue;
//    }


    @Override
    public UMCMessage sendSyncMsg( UMCMessage request, boolean bNoneBuffered, long nWaitTime ) throws IOException {
        return this.nextSynChannelCB().sendSyncMsg( request, bNoneBuffered, nWaitTime );
    }

    @Override
    public void sendAsynMsg( UMCMessage request, boolean bNoneBuffered ) throws IOException {
        this.nextAsyChannelCB().sendAsynMsg( request, bNoneBuffered );
    }

    @Override
    public void sendAsynMsg( UMCMessage request, boolean bNoneBuffered, UlfAsyncMsgHandleAdapter handler ) throws IOException {
        UlfAsyncMessengerChannelControlBlock cb = this.nextAsyChannelCB();
        if ( handler != null ) {
            // If the handler is null, do not set it; otherwise, it will disrupt the subsequent handler-setting pipeline.
            // Additionally, if there is no-response request, it will not affect the later pipeline.
            // 如果 handler 为 null 不要设置, 否则破坏后面的设置流水线，且无响应的请求不会影响后面的流水线.
            cb.pushMsgHandle( handler );
            //cb.getChannel().getNativeHandle().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY ) ).set( handler );
        }
        cb.sendAsynMsg( request, bNoneBuffered );
    }

    protected static void reconnect( ChannelControlBlock block, long mils ) throws IOException {
        if( block.isShutdown() ) {
            block.getChannel().reconnect( mils );
            ( (UlfMessageNode)block.getParentMessageNode() ).getChannelPool().setIdleChannel( block );
        }
    }

    protected static long getSyncWaitingMillis( Messenger messenger ) {
        return messenger.getConnectionArguments().getSyncWaitingMillis();
    }

    public static void reconnect( ChannelControlBlock block, Messenger messenger ) throws IOException {
        long mils = ArchAsyncMessenger.getSyncWaitingMillis( messenger );
        ArchAsyncMessenger.reconnect( block, mils );
    }

    public static void reconnect( ChannelControlBlock block, Messenger messenger, Object context ) throws IOException, MediumTerminationException {
        if ( context instanceof ChannelHandlerContext ) {
            ChannelHandlerContext ctx = (ChannelHandlerContext) context;
            EventLoop loop = ctx.channel().eventLoop();

            if ( !loop.isShuttingDown() ) {
                reconnect( block, messenger );
            }
            else {
                throw new MediumTerminationException( "Medium has already terminated." );
            }
        }
        else {
            reconnect( block, messenger );
        }
    }

}
