package com.pinecone.hydra.orchestration;

public class ContinuePoint extends ArchIrrevocableController implements ContinueController {
    public ContinuePoint() {
        super();
    }

    @Override
    public void call() throws BranchControlException {
        this.start();
    }
}
