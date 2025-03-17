package com.pinecone.hydra.storage.version.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface VersionMapping extends Pinenut {
    GUID getVersionGuid();
    void setVersionGuid(GUID versionGuid);
    GUID getFileGuid();
    void setFileGuid(GUID fileGuid);
    GUID getEnableVersionGuid();
    void setEnableVersionGuid(GUID enableVersionGuid);

}
