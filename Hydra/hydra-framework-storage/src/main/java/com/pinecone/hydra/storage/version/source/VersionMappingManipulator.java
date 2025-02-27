package com.pinecone.hydra.storage.version.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.version.entity.TitanVersionMapping;
import com.pinecone.hydra.storage.version.entity.VersionMapping;

import java.util.List;

public interface VersionMappingManipulator extends Pinenut {
      void insert(VersionMapping versionMapping);
       void remove(VersionMapping versionMapping);

       TitanVersionMapping queryVersionMapping(GUID fileGuid);

       void update(VersionMapping versionMapping);

       List<TitanVersionMapping> queryAllVersionMapper();

       boolean isExistEnableVersionMapping(GUID enableVersionGuid);
}
