package com.pinecone.hydra.umc.wolfmc.server;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

/**
 * @Description
 * @Author welsir
 * @Date 2024/6/11 23:34
 */
public abstract class AbstractTimerTask implements TimerTask {

    @Override
    public void run( Timeout timeout ) {
//        Collection<UlfRecipientChannelControlBlock > allChannels = NettyServerChannelRecordPool.getAllChannels();
//        for ( UlfRecipientChannelControlBlock channel : allChannels ) {
//            if (!channel.isShutdown()) {
//                doTask(channel);
//            }
//        }
    }

    protected abstract void doTask( ChannelControlBlock channel );

}
