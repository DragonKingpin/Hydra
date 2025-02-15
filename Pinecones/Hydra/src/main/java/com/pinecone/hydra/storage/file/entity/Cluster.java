package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface Cluster extends Pinenut {
    long getEnumId();

    void setEnumId(long enumId);

    GUID getFileGuid();
    void setFileGuid(GUID fileGuid);

    GUID getSegGuid();
    void setSegGuid(GUID segGuid);

    long getSegId();
    void setSegId(long segId);

    long getCrc32();
    void setCrc32(long crc32);

    long getSize();
    void setSize(long size);
    void save();
    void remove();

    default LocalCluster evinceLocalCluster(){
        return null;
    }
    default RemoteCluster evinceRemoteCluster(){
        return null;
    }
}
