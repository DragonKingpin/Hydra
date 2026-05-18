package com.pinecone.hydra.storage.volume;

import com.pinecone.hydra.storage.volume.config.TitanVolumeRuntimeConfig;

import java.util.Map;

public class KernelVolumeConfig extends TitanVolumeRuntimeConfig {
    public KernelVolumeConfig() {
        super();
    }

    public KernelVolumeConfig( Map<String, Object> config ) {
        super( config );
    }
}
