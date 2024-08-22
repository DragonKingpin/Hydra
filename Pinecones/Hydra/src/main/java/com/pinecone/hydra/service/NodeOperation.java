package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NodeOperation extends Pinenut {
    GUID addOperation(NodeInformation nodeInformation);
    void deleteOperation(GUID guid);
    NodeInformation SelectOperation(GUID guid);
    void UpdateOperation(NodeInformation nodeInformation);
}
