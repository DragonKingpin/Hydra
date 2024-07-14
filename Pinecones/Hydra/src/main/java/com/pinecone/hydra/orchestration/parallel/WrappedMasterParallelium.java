package com.pinecone.hydra.orchestration.parallel;

import com.pinecone.hydra.orchestration.Exertion;

public class WrappedMasterParallelium extends ArchMasterParallelium {
    protected Exertion mWrapped;

    public WrappedMasterParallelium( Exertion exertion ) {
        this.mWrapped = exertion;
    }

    @Override
    protected void doStart() {
        this.mWrapped.start();
    }

    @Override
    protected void doTerminate() {
        this.mWrapped.terminate();
    }

    @Override
    protected void doRollback() {
        this.mWrapped.rollback();
    }
}
