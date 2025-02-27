package com.pinecone.hydra.storage.version.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.version.entity.Version;

import java.util.List;

public interface VersionManipulator extends Pinenut {
    void insertObjectVersion(Version version);

    void removeObjectVersion( String version, GUID fileGuid );

    GUID queryObjectGuid( String version, GUID fileGuid );

    boolean queryIsManage(GUID fileGuid);

    List<GUID> fetchVersions(GUID guid);

    GUID getVersionFileByGuid(GUID fileGuid);
}
