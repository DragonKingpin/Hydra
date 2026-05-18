package com.pinecone.hydra.storage.volume.config;

import com.pinecone.hydra.storage.volume.VolumeConfig;

import java.util.HashMap;
import java.util.Map;

public class TitanVolumeRuntimeConfig implements VolumeConfig {
    protected String mTitanHomeDirectory;
    protected String mVolumeDataDirectory;
    protected String mBlockBackingFileName;
    protected String mObjectDataDirectory;
    protected String mChunkFilePrefix;
    protected String mChunkFileExtension;
    protected long   mDefaultAllocationUnit;

    public TitanVolumeRuntimeConfig() {
        this( new HashMap<>() );
    }

    public TitanVolumeRuntimeConfig( Map<String, Object> config ) {
        this.mTitanHomeDirectory    = this.stringValue( config, "TitanHomeDirectory", TitanVolumeSchema.TitanHomeDirectory );
        this.mVolumeDataDirectory   = this.stringValue( config, "VolumeDataDirectory", TitanVolumeSchema.VolumeDataDirectory );
        this.mBlockBackingFileName  = this.stringValue( config, "BlockBackingFileName", TitanVolumeSchema.BlockBackingFileName );
        this.mObjectDataDirectory   = this.stringValue( config, "ObjectDataDirectory", TitanVolumeSchema.ObjectDataDirectory );
        this.mChunkFilePrefix       = this.stringValue( config, "ChunkFilePrefix", TitanVolumeSchema.ChunkFilePrefix );
        this.mChunkFileExtension    = this.stringValue( config, "ChunkFileExtension", TitanVolumeSchema.ChunkFileExtension );
        this.mDefaultAllocationUnit = this.longValue( config, "DefaultAllocationUnit", TitanVolumeDefaults.DefaultAllocationUnit );
    }

    protected String stringValue( Map<String, Object> config, String key, String defaultValue ) {
        Object value = config.get( key );
        return value == null ? defaultValue : value.toString();
    }

    protected long longValue( Map<String, Object> config, String key, long defaultValue ) {
        Object value = config.get( key );
        return value instanceof Number ? ( (Number) value ).longValue() : defaultValue;
    }

    @Override
    public String getTitanHomeDirectory() {
        return this.mTitanHomeDirectory;
    }

    @Override
    public String getVolumeDataDirectory() {
        return this.mVolumeDataDirectory;
    }

    @Override
    public String getBlockBackingFileName() {
        return this.mBlockBackingFileName;
    }

    @Override
    public String getObjectDataDirectory() {
        return this.mObjectDataDirectory;
    }

    @Override
    public String getChunkFilePrefix() {
        return this.mChunkFilePrefix;
    }

    @Override
    public String getChunkFileExtension() {
        return this.mChunkFileExtension;
    }

    @Override
    public long getDefaultAllocationUnit() {
        return this.mDefaultAllocationUnit;
    }
}
