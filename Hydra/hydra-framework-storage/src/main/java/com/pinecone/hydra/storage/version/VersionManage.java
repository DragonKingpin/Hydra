package com.pinecone.hydra.storage.version;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.version.entity.TitanVersion;
import com.pinecone.hydra.storage.version.entity.TitanVersionMapping;
import com.pinecone.hydra.storage.version.entity.VersionMapping;

import java.util.List;

public interface VersionManage extends Pinenut {
    void insert(TitanVersion version);

    void remove(String version, GUID fileGuid);

    GUID queryObjectGuid(String version, GUID fileGuid );

    boolean queryIsManage(GUID targetStorageObjectGuid);

    List<GUID> fetchVersions(GUID guid);

    GUID getVersionFileByGuid( GUID fileGuid );

    TitanVersion queryByTargetStorageObjectGuid(GUID targetStorageObjectGuid);

    boolean isExistEnableVersionMapping(GUID enableVersionGuid);

    VersionMapping queryVersionMapping(GUID fileGuid);

    void UpdateVesionMapping(VersionMapping versionMapping);

    void insertVesionMapping(TitanVersionMapping versionMapping);
}
