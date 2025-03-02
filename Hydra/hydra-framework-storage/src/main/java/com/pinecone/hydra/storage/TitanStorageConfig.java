package com.pinecone.hydra.storage;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.Map;

public class TitanStorageConfig implements StorageConfig{
    protected String mszLocalHostGuid;

    protected String mszDefaultVolumeGuid;

    protected String mszDefaultTempFilePath;
    public TitanStorageConfig(){}

    public TitanStorageConfig(Map<String, Object> config){
        this.mszLocalHostGuid = (String) config.get("LocalHostGuid");
        this.mszDefaultVolumeGuid = (String) config.get("DefaultVolumePath");
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
