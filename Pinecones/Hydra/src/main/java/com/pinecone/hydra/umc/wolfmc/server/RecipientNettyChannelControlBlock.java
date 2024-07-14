package com.pinecone.hydra.umc.wolfmc.server;

import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.wolfmc.ArchChannelControlBlock;
import com.pinecone.hydra.umc.wolfmc.UlfChannel;
import com.pinecone.hydra.umc.wolfmc.UlfMCReceiver;
import com.pinecone.hydra.umc.wolfmc.UlfMCTransmit;
import io.netty.channel.Channel;

import java.util.concurrent.locks.Lock;

public class RecipientNettyChannelControlBlock extends ArchChannelControlBlock implements UlfRecipientChannelControlBlock {
    protected WolfMCServer mParentRecipient;

    public RecipientNettyChannelControlBlock( WolfMCServer recipient, UlfChannel channel, boolean bForceSyncMode ) {
        super( recipient, channel, bForceSyncMode );
        this.mParentRecipient  = recipient;
    }

    public RecipientNettyChannelControlBlock( WolfMCServer recipient, Channel nativeChannel, boolean bForceSyncMode ) {
        this( recipient, new UlfChannel( recipient, nativeChannel ), bForceSyncMode );
    }

    public RecipientNettyChannelControlBlock( WolfMCServer recipient, Channel nativeChannel ) {
        this( recipient, nativeChannel, false );
    }

    public RecipientNettyChannelControlBlock( WolfMCServer recipient, boolean bForceSyncMode ) {
        this( recipient, new UlfChannel( recipient ), bForceSyncMode );
    }

    public RecipientNettyChannelControlBlock( WolfMCServer recipient ) {
        this( recipient, false );
    }

    @Override
    public WolfMCServer getParentMessageNode() {
        return (WolfMCServer) super.getParentMessageNode();
    }

    protected void      afterConnectionArrive( Medium medium, boolean bRenew ) {
        super.afterConnectionArrive( medium, bRenew, this.getSynRequestLock() );
    }


    protected Lock      getSynRequestLock() {
        return this.getParentMessageNode().getSynRequestLock();
    }
}
