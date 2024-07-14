package com.pinecone.hydra.umc.msg;

import java.util.Queue;

public interface FairChannelPool extends ChannelPool {
    long getMajorWaitTimeout();

    FairChannelPool setMajorWaitTimeout( long nMillisTimeout );


    FairChannelPool pushBack( ChannelControlBlock channel );

    ChannelControlBlock pop();

    FairChannelPool setIdleChannel( ChannelControlBlock block );

    ChannelControlBlock nextSyncChannel( long nMillisTimeout, boolean bEager ) ;

    ChannelControlBlock nextAsynChannel( long nMillisTimeout, boolean bEager ) ;


    default ChannelControlBlock nextSyncChannel( long nMillisTimeout ) {
        return this.nextSyncChannel( nMillisTimeout, true );
    }

    default ChannelControlBlock nextAsynChannel( long nMillisTimeout ) {
        return this.nextSyncChannel( nMillisTimeout, true );
    }

    default ChannelControlBlock nextSyncChannel() {
        return this.nextSyncChannel( 5000 );
    }

    default ChannelControlBlock nextAsynChannel() {
        return this.nextSyncChannel( 5000 );
    }

    Queue getMajorQueue();
}
