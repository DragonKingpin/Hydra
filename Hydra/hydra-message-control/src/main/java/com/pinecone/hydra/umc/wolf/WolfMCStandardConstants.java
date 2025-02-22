package com.pinecone.hydra.umc.wolf;

import io.netty.channel.Channel;

public abstract class WolfMCStandardConstants {
    public static final String CB_CONTROL_BLOCK_KEY         = "ControlBlock";
    public static final String CB_ASYNC_MSG_HANDLE_KEY      = "AsyncMsgHandle";
    public static final String CB_ASY_EXCLUSIVE_HANDLE_KEY  = "AsyncExclusiveHandle";
    public static final String CB_EXTERNAL_CHANNEL_KEY      = "ExternalChannel";

    public static void copyChannelStandardAttrs( Channel leg, Channel neo ) {
        UlfChannel.copyChannelAttr( leg, neo, WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY );
        UlfChannel.copyChannelAttr( leg, neo, WolfMCStandardConstants.CB_ASY_EXCLUSIVE_HANDLE_KEY );
        UlfChannel.copyChannelAttr( leg, neo, WolfMCStandardConstants.CB_EXTERNAL_CHANNEL_KEY );
    }
}
