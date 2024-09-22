package com.pinecone.hydra.auto;

public final class Terminate extends ArchInstructation {
    Terminate () { }

    @Override
    public void execute() throws Exception {
        throw new InstantKillException();
    }
}