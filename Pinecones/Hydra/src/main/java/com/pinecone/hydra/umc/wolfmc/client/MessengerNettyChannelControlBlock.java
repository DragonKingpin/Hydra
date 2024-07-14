package com.pinecone.hydra.umc.wolfmc.client;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.wolfmc.*;
import io.netty.channel.Channel;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class MessengerNettyChannelControlBlock extends ArchChannelControlBlock implements UlfAsyncMessengerChannelControlBlock {
    protected ArchAsyncMessenger          mParentMessenger;
    protected BlockingDeque<UMCMessage >  mSyncRetMsgQueue = new LinkedBlockingDeque<>();

    public MessengerNettyChannelControlBlock( ArchAsyncMessenger messenger, UlfChannel channel, boolean bForceSyncMode ) {
        super( messenger, channel, bForceSyncMode );
        this.mParentMessenger  = messenger;
    }

    public MessengerNettyChannelControlBlock( ArchAsyncMessenger messenger, Channel nativeChannel, boolean bForceSyncMode ) {
        this( messenger, new UlfChannel( messenger, nativeChannel ), bForceSyncMode );
    }

    public MessengerNettyChannelControlBlock( ArchAsyncMessenger messenger, Channel nativeChannel ) {
        this( messenger, nativeChannel, false );
    }

    public MessengerNettyChannelControlBlock( ArchAsyncMessenger messenger, boolean bForceSyncMode ) {
        this( messenger, new UlfChannel( messenger ), bForceSyncMode );
    }

    public MessengerNettyChannelControlBlock( ArchAsyncMessenger messenger ) {
        this( messenger, false );
    }


    protected void                        afterConnectionArrive( Medium medium, boolean bRenew ) {
        super.afterConnectionArrive( medium, bRenew, this.getSynRequestLock() );
    }


    BlockingDeque<UMCMessage >            getSyncRetMsgQueue() {
        return this.mSyncRetMsgQueue;
    }


    @Override
    public Lock                           getSynRequestLock() {
        return this.getParentMessageNode().getSynRequestLock();
    }

    @Override
    public ArchAsyncMessenger             getParentMessageNode() {
        return (ArchAsyncMessenger) super.getParentMessageNode();
    }

    protected UMCMessage                  onlySendSyncMsg( UMCMessage message, boolean bNoneBuffered, long nWaitTime ) throws IOException {
        UMCMessage msg;

        this.mTransmit.sendMsg( message, bNoneBuffered );

        try{
            //msg = this.getParentMessageNode().getSyncRetMsgQueue().poll( nWaitTime, TimeUnit.MILLISECONDS );
            msg = this.getSyncRetMsgQueue().poll( nWaitTime, TimeUnit.MILLISECONDS );

            if( msg == null ) { // Close channel, preventing server sent messages late which could disrupted the sync deque.
                try{
                    this.getChannel().close();
                    ArchAsyncMessenger.reconnect( this );
                }
                catch ( ProvokeHandleException e ) {
                    if( e.getCause() instanceof IOException ) {
                        throw new IOException( e );
                    }
                }

                throw new IOException( "Waiting for receive synchronization message timeout [Max -> " + nWaitTime + " millis]." );
            }
        }
        catch ( InterruptedException e ) {
            msg = null;
        }

        return msg;
    }

    @Override
    public UMCMessage                     sendSyncMsg( UMCMessage message, boolean bNoneBuffered, long nWaitTime ) throws IOException {
        if( this.mbForceSyncMode ) {
            return this.onlySendSyncMsg( message, bNoneBuffered, nWaitTime );
        }
        else {
            this.getSynRequestLock().lock();
            UMCMessage msg = null;
            try{
                this.mbInSyncMode = true;
                this.getChannel().setChannelStatus( UlfChannelStatus.FORCE_SYNCHRONIZED );
                msg = this.onlySendSyncMsg( message, bNoneBuffered, nWaitTime );
                this.getParentMessageNode().getChannelPool().setIdleChannel( this ); // There will to set channel status.
                this.mbInSyncMode = false;
            }
            finally {
                this.getSynRequestLock().unlock();
            }
            return msg;
        }
    }

    @Override
    public void                           sendAsynMsg( UMCMessage request, boolean bNoneBuffered ) throws IOException {
        super.sendMsg( request, bNoneBuffered );
    }

    @Override
    public void                           release() {
        super.release();
        this.mParentMessenger    = null;
        this.mSyncRetMsgQueue    = null;
    }

}
