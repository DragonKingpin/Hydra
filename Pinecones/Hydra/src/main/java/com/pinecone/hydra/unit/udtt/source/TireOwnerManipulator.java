package com.pinecone.hydra.unit.udtt.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

import java.util.List;

public interface TireOwnerManipulator extends Pinenut {
    void insert(GUID subordinateGuid, GUID ownerGuid);

    void remove(GUID subordinateGuid,GUID ownerGuid);

    void removeBySubordinate(GUID subordinateGuid);

    void removeByOwner(GUID OwnerGuid);

    GUID getOwner(GUID subordinateGuid);

    List<GUID> getSubordinates(GUID guid);
    GUIDDistributedTrieNode checkOwned(GUID guid);
    void setOwned(GUID sourceGuid,GUID targetGuid);
    void setReparse(GUID sourceGuid,GUID targetGuid);
    String getLinkedType(GUID childGuid,GUID parentGuid);
}
