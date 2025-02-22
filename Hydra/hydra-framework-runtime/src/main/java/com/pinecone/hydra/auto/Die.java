package com.pinecone.hydra.auto;

public final class Die extends ArchInstructation implements InstantInstructation {
    Die () { }

    @Override
    public void execute() throws Exception {
        throw new InstantKillException();
    }
}
