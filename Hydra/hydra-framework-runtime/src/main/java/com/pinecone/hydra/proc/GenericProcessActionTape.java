package com.pinecone.hydra.proc;

public class GenericProcessActionTape implements ProcessActionTape {

    protected Throwable mLastError;

    protected int mExitCode;

    public GenericProcessActionTape() {

    }

    @Override
    public Throwable getLastError() {
        return this.mLastError;
    }

    @Override
    public void setLastError( Throwable lastError ) {
        this.mLastError = lastError;
    }

    @Override
    public int getExitCode() {
        return this.mExitCode;
    }

    @Override
    public void setExitCode( int exitCode ) {
        this.mExitCode = exitCode;
    }
}
