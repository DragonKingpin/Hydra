package com.pinecone.hydra.storage.volume.config;

public final class TitanVolumeSchema {
    public static final String TitanHomeDirectory   = ".titan";
    public static final String VolumeDataDirectory  = "data";
    public static final String BlockBackingFileName = "volume.bin";
    public static final String ObjectDataDirectory  = "data";
    public static final String ChunkFilePrefix      = "chunk-";
    public static final String ChunkFileExtension   = ".chunk";

    private TitanVolumeSchema() {
    }
}
