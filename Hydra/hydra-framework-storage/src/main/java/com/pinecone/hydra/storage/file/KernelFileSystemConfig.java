package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.ArchStorageConfig;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.volume.VolumeConstants;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.Map;

public class KernelFileSystemConfig extends ArchStorageConfig implements FileSystemConfig {
    protected String mszVersionSignature        = FileConstants.StorageVersionSignature;
    protected Number mnClusterSize              = FileConstants.DefaultClusterSize;
    protected GUID   mLocalhostGUID             = StorageConstants.LocalhostGUID;
    protected Number mTinyFileStripSizing       = VolumeConstants.TinyFileStripSizing;
    protected long mPathQueryExpiryTimeHotMil   = DefaultCacheConstants.PathQueryExpiryTimeHotMil;

    public KernelFileSystemConfig() {
        super();
    }

    public KernelFileSystemConfig( Map<String, Object> config ) {
        super( config );
        this.mszVersionSignature           = (String) config.getOrDefault("VersionSignature", FileConstants.StorageVersionSignature);
        this.mnClusterSize                 = (Number) config.getOrDefault("ClusterSize", FileConstants.DefaultClusterSize);
        this.mLocalhostGUID                = GUIDs.GUID72( String.valueOf(config.getOrDefault("LocalhostGUID", StorageConstants.LocalhostGUID)) );
        this.mTinyFileStripSizing          = (Number) config.getOrDefault("TinyFileStripSizing", VolumeConstants.TinyFileStripSizing);
        this.mPathQueryExpiryTimeHotMil    = ((Number) config.getOrDefault("PathQueryExpiryTimeHotMil", DefaultCacheConstants.PathQueryExpiryTimeHotMil)).longValue();
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
    public long getPathQueryExpiryTimeHotMil() {
        return this.mPathQueryExpiryTimeHotMil       ;
    }



}
