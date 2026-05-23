package com.pinecone.hydra.umct.husky.heartbeat;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.vita.HeartbeatControl;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

public class HuskyHeartbeatControl implements HeartbeatControl {
    protected final HashedWheelTimer                            mTimer;
    protected final ConcurrentMap<ChannelControlBlock, Timeout> mHeartbeatTasks;

    protected static int nextPowerOfTwo( int num ) {
        int n = 1;
        while (n < num) {
            n <<= 1;
        }
        return n;
    }

    public static HashedWheelTimer createTimer( long heartIntervalMillis ) {
        // tickDuration ∈ [ 100ms, 1s ]
        // Tick too short (<100ms) results in high CPU polling.
        // Tick too long (>1s) causes significant heartbeat delay.
        // Optimal range is between 250ms and 500ms.
        // 40 is an empirical value for balancing, ensuring that heartbeats are evenly distributed on the time wheel (helps avoid instantaneous load).

        // Tick 过短（<100ms）CPU 轮询过高
        // Tick 过长（>1s）心跳延迟较大
        // 取 250ms ~ 800ms 较优
        // 40 是均分调优经验值，让心跳能在时间轮上较好地分布均匀（较好地避免瞬时负载）
        long tickDuration = Math.min( Math.max( heartIntervalMillis / 40, 250 ), 800 );

        // Ensure time-wheel covered all HeartbeatIntervals.
        int ticksPerWheel = (int) Math.ceil( (double) heartIntervalMillis / tickDuration );

        // Adjust ticksPerWheel => 2^N
        ticksPerWheel = HuskyHeartbeatControl.nextPowerOfTwo( ticksPerWheel );

        return new HashedWheelTimer( tickDuration, TimeUnit.MILLISECONDS, ticksPerWheel );
    }

    public HuskyHeartbeatControl( long heartIntervalMillis ) {
        this.mTimer          = HuskyHeartbeatControl.createTimer( heartIntervalMillis );
        //this.mTimer          = new HashedWheelTimer( 100, TimeUnit.MILLISECONDS, 512 );
        this.mHeartbeatTasks = new ConcurrentHashMap<>();
    }

    @Override
    public void registerChannels( Collection<ChannelControlBlock> channels, long intervalMillis ) {
        for ( ChannelControlBlock ccb : channels ) {
            this.registerChannel( ccb, intervalMillis );
        }
    }

    @Override
    public void registerChannel( ChannelControlBlock ccb, long intervalMillis ) {
        Timeout oldTimeout = this.mHeartbeatTasks.get( ccb );
        if ( oldTimeout != null && !oldTimeout.isCancelled() && !oldTimeout.isExpired() ) {
            return;
        }

        if ( oldTimeout != null ) {
            this.mHeartbeatTasks.remove( ccb, oldTimeout );
        }

        Timeout timeout = this.scheduleHeartbeat( ccb, intervalMillis );
        this.mHeartbeatTasks.put( ccb, timeout );
    }

    @Override
    public void deregisterChannel( ChannelControlBlock ccb ) {
        Timeout timeout = this.mHeartbeatTasks.remove( ccb );
        if ( timeout != null ) {
            timeout.cancel();
        }
    }

    protected Timeout scheduleHeartbeat( ChannelControlBlock ccb, long intervalMillis ) {
        return this.mTimer.newTimeout( new HeartbeatTask( ccb, intervalMillis ), intervalMillis, TimeUnit.MILLISECONDS );
    }

    protected boolean isCurrentTask( ChannelControlBlock ccb, Timeout timeout ) {
        return this.mHeartbeatTasks.get( ccb ) == timeout;
    }

    protected void removeTask( ChannelControlBlock ccb, Timeout timeout ) {
        this.mHeartbeatTasks.remove( ccb, timeout );
    }

    protected class HeartbeatTask implements TimerTask {
        private final ChannelControlBlock  mChannelControlBlock;
        private final long                 mIntervalMillis;

        HeartbeatTask( ChannelControlBlock ccb, long intervalMillis ) {
            this.mChannelControlBlock = ccb;
            this.mIntervalMillis = intervalMillis;
        }

        @Override
        public void run( Timeout timeout ) throws IOException {
            if ( !HuskyHeartbeatControl.this.isCurrentTask( this.mChannelControlBlock, timeout ) ) {
                return;
            }

            if ( this.mChannelControlBlock.isShutdown() ) {
                HuskyHeartbeatControl.this.removeTask( this.mChannelControlBlock, timeout );
                return;
            }

            try {
                HuskyHeartbeatControl.this.sendHeartbeat( this.mChannelControlBlock );
                Timeout newTimeout = HuskyHeartbeatControl.this.scheduleHeartbeat( this.mChannelControlBlock, this.mIntervalMillis );
                HuskyHeartbeatControl.this.mHeartbeatTasks.replace( this.mChannelControlBlock, timeout, newTimeout );
            }
            catch ( IOException e ) {
                HuskyHeartbeatControl.this.removeTask( this.mChannelControlBlock, timeout );
                throw e;
            }
        }
    }

    protected void sendHeartbeat( ChannelControlBlock ccb ) throws IOException {
        if ( ccb.getChannelStatus().isAsynAvailable() && !ccb.isShutdown() ) {
            ccb.sendMsg( HeartbeatConstants.HCTP_HEART_ALIVE, true );
        }
    }

    @Override
    public boolean interceptFeedback( ChannelControlBlock block, UMCMessage msg ) throws IOException {
        int nControlBits = msg.getHead().getControlBits();
        if ( nControlBits == HeartbeatConstants.HCTP_HEART_RESPONSE_ACK ) {
            //Debug.traceSyn( msg );
            // Do nothing. [Keep the format]
            return true;
        }
        return false;
    }

    @Override
    public void shutdown() {
        this.mTimer.stop();
        this.mHeartbeatTasks.clear();
    }
}
