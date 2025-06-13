package com.pinecone.hydra.proc;

import java.util.Map;

public class UniformProcessConfig implements ProcessConfig {
    protected String mPathSeparator = ProcessConstants.PathSeparator;

    public UniformProcessConfig(){}

    public UniformProcessConfig(Map<String, Object> config) {
        this.mPathSeparator = (String) config.getOrDefault("PathSeparator", ProcessConstants.PathSeparator);
    }

    @Override
    public String getPathSeparator() {
        return this.mPathSeparator;
    }
}
