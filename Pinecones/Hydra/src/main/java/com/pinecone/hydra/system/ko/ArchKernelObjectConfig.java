package com.pinecone.hydra.system.ko;

public abstract class ArchKernelObjectConfig implements KernelObjectConfig {
    protected String mszPathNameSeparator = KernelObjectConstants.PathNameSeparator;

    protected String mszFullNameSeparator = KernelObjectConstants.FullNameSeparator;

    protected String mszPathNameSepRegex  = KernelObjectConstants.PathNameSepRegex;

    protected String mszFullNameSepRegex  = KernelObjectConstants.FullNameSepRegex;

    @Override
    public String getPathNameSeparator() {
        return this.mszPathNameSeparator;
    }

    @Override
    public String getFullNameSeparator() {
        return this.mszFullNameSeparator;
    }

    @Override
    public String getPathNameSepRegex() {
        return this.mszPathNameSepRegex;
    }

    @Override
    public String getFullNameSepRegex() {
        return this.mszFullNameSepRegex;
    }
}
