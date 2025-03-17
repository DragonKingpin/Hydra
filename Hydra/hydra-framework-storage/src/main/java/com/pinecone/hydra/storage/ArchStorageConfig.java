package com.pinecone.hydra.storage;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.Map;

public abstract class ArchStorageConfig extends ArchKernelObjectConfig implements StorageConfig {
    protected String mszLocalHostGuid = StorageConstants.LocalhostGUID.toString();

    protected String mszDefaultVolumeGuid   ;

    protected String mszDefaultTempFilePath ;
    protected ArchStorageConfig(){
        super();
    }

    public ArchStorageConfig( Map<String, Object> config ){
        super( config );
        this.mszLocalHostGuid       = (String) config.getOrDefault("LocalHostGuid", StorageConstants.LocalhostGUID.toString());
        this.mszDefaultVolumeGuid   = (String) config.get("DefaultVolumeGuid");
        this.mszDefaultTempFilePath = (String) config.get("DefaultTempFilePath");
    }

    @Override
    public GUID getLocalHostGuid() {
        return GUIDs.GUID72(this.mszLocalHostGuid);
    }

    @Override
    public String getDefaultVolumeGuid() {
        return this.mszDefaultVolumeGuid;
    }

    @Override
    public String getDefaultTempFilePath() {
        return this.mszDefaultTempFilePath;
    }
}
