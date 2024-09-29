package com.pinecone.hydra.system.ko;

public abstract class ArchKernelObjectConfig implements KernelObjectConfig {
    protected String mszPathNameSeparator = KernelObjectConstants.PathNameSeparator;

    protected String mszFullNameSeparator = KernelObjectConstants.FullNameSeparator;

    @Override
    public String getPathNameSeparator() {
        return this.mszPathNameSeparator;
    }

    @Override
    public String getFullNameSeparator() {
        return this.mszFullNameSeparator;
    }
}
