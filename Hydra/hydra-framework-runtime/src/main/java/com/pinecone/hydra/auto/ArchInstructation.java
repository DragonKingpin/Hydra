package com.pinecone.hydra.auto;

public abstract class ArchInstructation implements Instructation {
    protected Exception  mLastException;

    protected ArchInstructation() {

    }

    public Exception lastException() {
        return this.mLastException;
    }

    public void setLastException( Exception e ) {
        this.mLastException = e;
    }
}
