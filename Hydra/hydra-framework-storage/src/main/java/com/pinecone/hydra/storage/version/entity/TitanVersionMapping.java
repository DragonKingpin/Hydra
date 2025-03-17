package com.pinecone.hydra.storage.version.entity;

import com.pinecone.framework.util.id.GUID;

public class TitanVersionMapping implements VersionMapping{
    GUID versionGuid;
    GUID fileGuid;
    GUID enableVersionGuid;

    public TitanVersionMapping() {
    }

    public TitanVersionMapping(GUID versionGuid, GUID fileGuid, GUID enableVersionGuid) {
        this.versionGuid = versionGuid;
        this.fileGuid = fileGuid;
        this.enableVersionGuid = enableVersionGuid;
    }

    public GUID getVersionGuid() {
        return versionGuid;
    }

    public void setVersionGuid(GUID versionGuid) {
        this.versionGuid = versionGuid;
    }

    public GUID getFileGuid() {
        return fileGuid;
    }

    public void setFileGuid(GUID fileGuid) {
        this.fileGuid = fileGuid;
    }

    public GUID getEnableVersionGuid() {
        return enableVersionGuid;
    }

    public void setEnableVersionGuid(GUID enableVersionGuid) {
        this.enableVersionGuid = enableVersionGuid;
    }
}
