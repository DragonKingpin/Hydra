package com.pinecone.hydra.system;

public class SystemSkeleton extends ArchSystemCascadeComponentManager implements HySkeleton {
    public SystemSkeleton( Hydrogen system ){
        super( system );
    }

    @Override
    public Hydrogen getSystem() {
        return this.mSystem;
    }
}
