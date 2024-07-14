package com.pinecone.hydra.orchestration;

public abstract class ArchIrrevocableController extends ArchExertion implements ProcessController {
    public ArchIrrevocableController() {
        super();
    }

    @Override
    public void doStart() {
        // Marking state for DFA.
    }

    @Override
    public void doTerminate() {
    }

    @Override
    public void doRollback() {
        // Do nothing, 'Irrevocable Controller (e.g. Break, Continue)' can`t directly withdraw.
    }
}
