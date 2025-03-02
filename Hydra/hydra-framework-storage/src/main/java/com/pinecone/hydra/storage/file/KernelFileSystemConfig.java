package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.volume.VolumeConstants;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

import java.util.Map;

public class KernelFileSystemConfig extends ArchKernelObjectConfig implements FileSystemConfig {
    protected String mszVersionSignature    = FileConstants.StorageVersionSignature;
    protected Number mnClusterSize          = FileConstants.DefaultClusterSize;
    protected GUID   mLocalhostGUID         = StorageConstants.LocalhostGUID;
    protected Number mTinyFileStripSizing = VolumeConstants.TinyFileStripSizing;
    protected String mDefaultVolumePath = StorageConstants.DefaultVolumeGuid;
    protected long mDefaultExpiryTime = DefaultCacheConstants.PathQueryExpiryTimeHotMil;
    protected int mRedisTimeOut = FileConstants.REDIS_TIME_OUT;

    public KernelFileSystemConfig(){
    }

    public KernelFileSystemConfig(Map<String, Object> config){
        this.mszVersionSignature = (String) config.get("VersionSignature");
        this.mnClusterSize = (Number) config.get("ClusterSize");
        this.mLocalhostGUID = (GUID) config.get("LocalhostGUID");
        this.mTinyFileStripSizing = (Number) config.get("TinyFileStripSizing");
        this.mDefaultExpiryTime = ((Number) config.get("DefaultExpiryTime")).longValue();
        this.mDefaultVolumePath = (String) config.get("DefaultVolumePath");
        this.mRedisTimeOut = ((Number)config.get("RedisTimeOut")).intValue();
    }


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
    public Number getmTinyFileStripSizing() {
        return this.mTinyFileStripSizing;
    }

    @Override
    public String getDefaultVolume() {
        return this.mDefaultVolumePath;
    }

    @Override
    public long getExpiryTime() {
        return this.mDefaultExpiryTime;
    }

    @Override
    public int getmRedisTimeOut() {
        return this.mRedisTimeOut;
    }
}
