package com.pinecone.hydra.orchestration;

public class BreakPoint extends ArchIrrevocableController implements BreakController  {
    public BreakPoint() {
        super();
    }

    @Override
    public void call() throws BranchControlException {
        this.start();
    }
}
