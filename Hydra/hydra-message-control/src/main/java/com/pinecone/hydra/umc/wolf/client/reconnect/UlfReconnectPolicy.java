package com.pinecone.hydra.umc.wolf.client.reconnect;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UlfReconnectPolicy extends Pinenut {
    long getInitialDelayMillis();

    long getMaxDelayMillis();

    int getMaxAttempts();

    long nextDelayMillis( int nAttempt );

    boolean shouldContinue( int nAttempt );
}
