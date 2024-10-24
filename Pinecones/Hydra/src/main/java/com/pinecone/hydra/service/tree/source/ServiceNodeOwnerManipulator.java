package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;

import java.util.List;

public interface ServiceNodeOwnerManipulator extends Pinenut {
    void insert(GUID subordinateGuid,GUID ownerGuid);
    void remove(GUID subordinateGuid,GUID ownerGuid);
    void removeBySubordinate(GUID subordinateGuid);
    GUID getOwner(GUID guid);
    List<GUID> getSubordinates(GUID guid);
    void update(GUID subordinateGuid,GUID ownerGuid);
}
