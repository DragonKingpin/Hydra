package com.pinecone.hydra.system.ko;

import java.util.Map;

public abstract class ArchKernelObjectConfig implements KernelObjectConfig {
    protected String mszPathNameSeparator = KernelObjectConstants.PathNameSeparator;

    protected String mszFullNameSeparator = KernelObjectConstants.FullNameSeparator;

    protected String mszPathNameSepRegex  = KernelObjectConstants.PathNameSepRegex;

    protected String mszFullNameSepRegex  = KernelObjectConstants.FullNameSepRegex;

    protected int    mnShortPathLength    = KernelObjectConstants.ShortPathLength;

    protected ArchKernelObjectConfig() {

    }

    public ArchKernelObjectConfig( Map<String, Object> config ){
        this.mszPathNameSeparator = (String) config.getOrDefault("PathNameSeparator", KernelObjectConstants.PathNameSeparator);
        this.mszFullNameSeparator = (String) config.getOrDefault("FullNameSeparator", KernelObjectConstants.FullNameSeparator);
        this.mszPathNameSepRegex  = (String) config.getOrDefault("PathNameSepRegex", KernelObjectConstants.PathNameSepRegex);
        this.mszFullNameSepRegex  = (String) config.getOrDefault("FullNameSepRegex", KernelObjectConstants.FullNameSepRegex);
        this.mnShortPathLength    = ( (Number) config.getOrDefault("ShortPathLength", KernelObjectConstants.ShortPathLength) ).intValue();
    }

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

    @Override
    public int getShortPathLength() {
        return this.mnShortPathLength;
    }
}
