package com.pinecone.hydra.proc;

public abstract class ArchProcessManager implements ProcessManager {

    protected abstract void expunge( UProcess that );

    public static void invokeExpunge( ArchProcessManager manager, UProcess that ) {
        manager.expunge( that );
    }

}
