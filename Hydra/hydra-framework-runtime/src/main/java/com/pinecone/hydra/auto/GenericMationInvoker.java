package com.pinecone.hydra.auto;

public class GenericMationInvoker implements MationInvoker {
    private long mMaxExecutionMillis;
    private long mMaxInterruptMillis;

    public GenericMationInvoker( long maxExecutionMillis, long maxInterruptMillis ) {
        this.mMaxExecutionMillis = maxExecutionMillis;
        this.mMaxInterruptMillis = maxInterruptMillis;
    }

    public GenericMationInvoker() {
        this( Long.MAX_VALUE, -1 );
    }


    @Override
    public long getMaxExecutionMillis() {
        return this.mMaxExecutionMillis;
    }

    @Override
    public long getMaxInterruptMillis() {
        return this.mMaxInterruptMillis;
    }

    @Override
    public void setMaxExecutionMillis( long maxExecutionMillis ) {
        this.mMaxExecutionMillis = maxExecutionMillis;
    }

    @Override
    public void setMaxInterruptMillis( long maxInterruptMillis ) {
        this.mMaxInterruptMillis = maxInterruptMillis;
    }
}
