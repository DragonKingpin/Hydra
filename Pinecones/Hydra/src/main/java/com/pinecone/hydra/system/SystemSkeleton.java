package com.pinecone.hydra.system;

public class SystemSkeleton extends ArchSystemCascadeComponentManager implements HySkeleton {
    public SystemSkeleton( Hydrarum system ){
        super( system );
    }

    @Override
    public Hydrarum getSystem() {
        return this.mSystem;
    }
}
