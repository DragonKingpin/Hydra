package com.pinecone.framework.system;

public interface CascadeSystem extends RuntimeSystem {
    CascadeSystem rootSystem();

    CascadeSystem getParent();

    default long getPrimaryId() {
        return 0;
    }

    default boolean isPrimarySystem() {
        return this.getPrimaryId() == this.getId();
    }
}
