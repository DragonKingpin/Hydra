package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface ConfNodeOwnerManipulator extends Pinenut {
    void insert(GUID subordinateGuid,GUID ownerGuid);

    void remove(GUID subordinateGuid,GUID ownerGuid);

    void removeBySubordinate(GUID subordinateGuid);

    void removeByOwner(GUID OwnerGuid);

    GUID getOwner(GUID subordinateGuid);

    List<GUID> getSubordinates(GUID guid);
}