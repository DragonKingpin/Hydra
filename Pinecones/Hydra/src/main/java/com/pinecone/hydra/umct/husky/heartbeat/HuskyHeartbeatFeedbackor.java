package com.pinecone.hydra.umct.husky.heartbeat;

import java.io.IOException;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.vita.HeartbeatFeedbackor;

public class HuskyHeartbeatFeedbackor implements HeartbeatFeedbackor {

    public HuskyHeartbeatFeedbackor() {

    }

    @Override
    public boolean interceptHeartbeat( ChannelControlBlock block, UMCMessage msg ) throws IOException {
        int nControlBits = msg.getHead().getControlBits();
        if ( nControlBits == HeartbeatConstants.HCTP_HEART_REQUEST_ALIVE ) {
            this.feedback( block, msg );
            return true;
        }
        return false;
    }

    @Override
    public void feedback( ChannelControlBlock block, UMCMessage msg ) throws IOException {
        if ( block.getChannelStatus().isAsynAvailable() && !block.isShutdown() ) {
            block.sendMsg( HeartbeatConstants.HCTP_HEART_ACK, true );
        }
    }

}
