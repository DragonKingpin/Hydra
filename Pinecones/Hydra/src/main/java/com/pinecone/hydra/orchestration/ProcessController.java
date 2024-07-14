package com.pinecone.hydra.orchestration;

public interface ProcessController extends Exertion {
    ProcessController BREAK    = new BreakPoint();
    ProcessController CONTINUE = new ContinuePoint();

    void call() throws BranchControlException ;
}
