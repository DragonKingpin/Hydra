package com.pinecone.hydra.auto;

public final class Continue extends ArchInstructation {
    Continue () { }

    @Override
    public void execute() throws Exception {
        throw new ContinueException();
    }
}