package com.pinecone.hydra.system;

public interface HyComponent extends SystemCascadeComponent {
    @Override
    Hydrogen getSystem();
}
