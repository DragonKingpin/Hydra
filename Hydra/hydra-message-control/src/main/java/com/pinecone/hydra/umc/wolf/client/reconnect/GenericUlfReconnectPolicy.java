package com.pinecone.hydra.umc.wolf.client.reconnect;

public class GenericUlfReconnectPolicy implements UlfReconnectPolicy {
    protected long   mnInitialDelayMillis;
    protected long   mnMaxDelayMillis;
    protected int    mnMaxAttempts;

    public GenericUlfReconnectPolicy() {
        this.mnInitialDelayMillis = 500;
        this.mnMaxDelayMillis     = 5000;
        this.mnMaxAttempts        = 0;
    }

    @Override
    public long getInitialDelayMillis() {
        return this.mnInitialDelayMillis;
    }

    @Override
    public long getMaxDelayMillis() {
        return this.mnMaxDelayMillis;
    }

    @Override
    public int getMaxAttempts() {
        return this.mnMaxAttempts;
    }

    @Override
    public long nextDelayMillis( int nAttempt ) {
        long nDelayMillis = this.mnInitialDelayMillis;

        for ( int i = 1; i < nAttempt; i++ ) {
            nDelayMillis = nDelayMillis * 2;
            if ( nDelayMillis >= this.mnMaxDelayMillis ) {
                return this.mnMaxDelayMillis;
            }
        }

        return nDelayMillis;
    }

    @Override
    public boolean shouldContinue( int nAttempt ) {
        if ( this.mnMaxAttempts <= 0 ) {
            return true;
        }

        return nAttempt < this.mnMaxAttempts;
    }
}
