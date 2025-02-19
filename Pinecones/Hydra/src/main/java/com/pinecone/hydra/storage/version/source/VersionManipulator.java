package com.pinecone.hydra.storage.version.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface VersionManipulator extends Pinenut {
    void insertObjectVersion(String version, GUID targetStorageObjectGuid, GUID fileGuid);

    void removeObjectVersion( String version, GUID fileGuid );

    GUID queryObjectGuid( String version, GUID fileGuid );

    boolean queryIsManage(GUID fileGuid);

    List<GUID> fetchVersions(GUID guid);
}
