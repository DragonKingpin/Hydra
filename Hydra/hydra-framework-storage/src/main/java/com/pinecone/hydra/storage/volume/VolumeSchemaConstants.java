package com.pinecone.hydra.storage.volume;

import com.pinecone.hydra.storage.volume.config.TitanVolumeDefaults;
import com.pinecone.hydra.storage.volume.config.TitanVolumeSchema;

public final class VolumeSchemaConstants {
    public static final String TitanHomeDirectory   = TitanVolumeSchema.TitanHomeDirectory;
    public static final String VolumeDataDirectory  = TitanVolumeSchema.VolumeDataDirectory;
    public static final String BlockBackingFileName = TitanVolumeSchema.BlockBackingFileName;
    public static final String ObjectDataDirectory  = TitanVolumeSchema.ObjectDataDirectory;
    public static final String ChunkFilePrefix      = TitanVolumeSchema.ChunkFilePrefix;
    public static final String ChunkFileExtension   = TitanVolumeSchema.ChunkFileExtension;

    public static final long DefaultAllocationUnit = TitanVolumeDefaults.DefaultAllocationUnit;

    private VolumeSchemaConstants() {
    }
}
