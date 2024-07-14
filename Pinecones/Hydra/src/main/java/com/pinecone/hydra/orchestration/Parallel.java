package com.pinecone.hydra.orchestration;

public interface Parallel extends Transaction {
    void notifyFinished  ( Exertion exertion );

    void notifyExecuting ( Exertion exertion );
}
