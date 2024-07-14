package com.pinecone.hydra.umc.wolfmc.server;


import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

/**
 * @Description
 * @Author welsir
 * @Date 2024/6/11 23:35
 */
public class IdleChannelTimerTask extends AbstractTimerTask {

    private final int idleTimeout;

    public IdleChannelTimerTask( int idleTimeout ) {
        this.idleTimeout = idleTimeout;
    }

    @Override
    protected void doTask( ChannelControlBlock channel ) {
        try {
            if(channel.isShutdown()){
                return;
            }
            long now = System.currentTimeMillis();
            boolean isReadTimeout = isReadTimeout(channel, now);
            boolean isWriteTimeout = isWriteTimeout(channel, now);

            if (isReadTimeout || isWriteTimeout) {
                Debug.echo("连接超时，尝试关闭连接....");
                channel.close();
                //NettyServerChannelRecordPool.removeChannel(channel);
            }
        }
        catch (Throwable t){
            throw new RuntimeException(t);
        }
    }

    protected boolean isReadTimeout( ChannelControlBlock channel, long now ) {
        Long lastRead = lastRead(channel);
        return lastRead != null && now - lastRead > idleTimeout;
    }

    protected boolean isWriteTimeout( ChannelControlBlock channel, long now ) {
        Long lastWrite = lastWrite(channel);
        return lastWrite != null && now - lastWrite > idleTimeout;
    }

    public Long lastRead( ChannelControlBlock channel ){
        return 0L;
        //return channel.getAttribute( IdleChannelHandler.KEY_READ_TIMESTAMP, Long.class );
    }

    public Long lastWrite( ChannelControlBlock channel ){
        return 0L;
        //return channel.getAttribute( IdleChannelHandler.KEY_WRITE_TIMESTAMP, Long.class );
    }

}
