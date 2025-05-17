package com.pinecone.hydra.system.ko.runtime;

import com.pinecone.hydra.system.ko.KernelObjectConfig;

public class GenericRuntimeInstrumentConfig implements KernelObjectConfig {
    protected String mszPathNameSeparator = "/";

    @Override
    public String getPathNameSeparator() {
        return this.mszPathNameSeparator;
    }

    @Override
    public String getFullNameSeparator() {
        return null;
    }

    @Override
    public String getPathNameSepRegex() {
        return null;
    }

    @Override
    public String getFullNameSepRegex() {
        return null;
    }

    @Override
    public int getShortPathLength() {
        return 0;
    }
}
