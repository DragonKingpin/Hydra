package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.volume.VolumeConstants;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class KernelFileSystemConfig extends ArchKernelObjectConfig implements FileSystemConfig {
    protected String mszVersionSignature    = FileConstants.StorageVersionSignature;
    protected Number mnClusterSize          = FileConstants.DefaultClusterSize;
    protected GUID   mLocalhostGUID         = StorageConstants.LocalhostGUID;
    protected Number TinyFileStripSizing    = VolumeConstants.TinyFileStripSizing;
    protected String DefaultVolumePath      = StorageConstants.DefaultVolumePath;
    protected long   DefaultExpiryTime      = DefaultCacheConstants.PathQueryExpiryTimeHotMil;
    protected int    RedisTimeOut           = FileConstants.REDIS_TIME_OUT;


    @Override
    public String getVersionSignature() {
        return this.mszVersionSignature;
    }

    public Number getClusterSize() {
        return this.mnClusterSize;
    }

    public GUID getLocalhostGUID() {
        return this.mLocalhostGUID;
    }

    @Override
    public Number getTinyFileStripSizing() {
        return this.TinyFileStripSizing;
    }

    @Override
    public String getDefaultVolume() {
        return this.DefaultVolumePath;
    }

    @Override
    public long getExpiryTime() {
        return this.DefaultExpiryTime;
    }

    @Override
    public int getRedisTimeOut() {
        return this.RedisTimeOut;
    }
}
