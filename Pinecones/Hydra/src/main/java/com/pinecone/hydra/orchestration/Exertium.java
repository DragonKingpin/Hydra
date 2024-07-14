package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.NotImplementedException;

public class Exertium extends ArchExertion {
    @Override
    protected void doStart() {
        throw new NotImplementedException();
    }

    @Override
    protected void doTerminate() {
        throw new NotImplementedException();
    }

    @Override
    protected void doRollback() {
        throw new NotImplementedException();
    }
}
