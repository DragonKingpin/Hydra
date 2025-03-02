package com.pinecone.hydra.storage.volume;

import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

import java.util.Map;

public class KernelVolumeConfig extends ArchKernelObjectConfig implements VolumeConfig {
    protected String mszVersionSignature             = StorageConstants.StorageVersionSignature;

    protected Number mnTinyFileStripSizing           = VolumeConstants.TinyFileStripSizing  ;
    protected Number mnSmallFileStripSizing          = VolumeConstants.SmallFileStripSizing ;
    protected Number mnMegaFileStripSizing           = VolumeConstants.MegaFileStripSizing  ;
    protected Number mnDefaultStripSize              = VolumeConstants.DefaultStripSize     ;
    protected int    mStripResidentCacheAllotRatio   = VolumeConstants.StripResidentCacheAllotRatio;
    protected String mStorageObjectExtension         = VolumeConstants.StorageObjectExtension;
    protected String mSqliteFileExtension            = VolumeConstants.SqliteFileExtension;
    protected String mPathSeparator                  = VolumeConstants.PathSeparator;

    public KernelVolumeConfig(){}

    public KernelVolumeConfig(Map<String, Object> config){
        this.mszVersionSignature = (String) config.get("VersionSignature");
        this.mnTinyFileStripSizing = (Number) config.get("TinyFileStripSizing");
        this.mnSmallFileStripSizing = (Number) config.get("SmallFileStripSizing");
        this.mnMegaFileStripSizing = (Number) config.get("MegaFileStripSizing");
        this.mnDefaultStripSize = (Number) config.get("DefaultStripSize");
        this.mStripResidentCacheAllotRatio = ((Number) config.get("StripResidentCacheAllotRatio")).intValue();
        this.mStorageObjectExtension = (String) config.get("StorageObjectExtension");
        this.mSqliteFileExtension = (String) config.get("SqliteFileExtension");
        this.mPathSeparator = (String) config.get("PathSeparator");
    }

    @Override
    public String getVersionSignature() {
        return this.mszVersionSignature;
    }

    @Override
    public Number getTinyFileStripSizing() {
        return this.mnTinyFileStripSizing;
    }

    @Override
    public Number getSmallFileStripSizing() {
        return this.mnSmallFileStripSizing;
    }

    @Override
    public Number getMegaFileStripSizing() {
        return this.mnMegaFileStripSizing;
    }

    @Override
    public Number getDefaultStripSize() {
        return this.mnDefaultStripSize;
    }

    @Override
    public int getStripResidentCacheAllotRatio() {
        return this.mStripResidentCacheAllotRatio;
    }

    @Override
    public String getStorageObjectExtension() {
        return this.mStorageObjectExtension;
    }

    @Override
    public String getSqliteFileExtension() {
        return this.mSqliteFileExtension;
    }

    @Override
    public String getPathSeparator() {
        return this.mPathSeparator;
    }
}
