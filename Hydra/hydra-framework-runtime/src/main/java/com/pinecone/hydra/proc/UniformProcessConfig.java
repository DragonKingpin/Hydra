package com.pinecone.hydra.proc;

import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

import java.util.Map;

public class UniformProcessConfig extends ArchKernelObjectConfig implements ProcessManagerConfig {

    public UniformProcessConfig( Map<String, Object> config ) {
        super(config);
    }

    public UniformProcessConfig(){
        super();
    }

}
