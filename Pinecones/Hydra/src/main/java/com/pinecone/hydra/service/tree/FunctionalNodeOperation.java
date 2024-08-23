package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface FunctionalNodeOperation extends Pinenut {
    GUID addOperation(FunctionalNodeInformation functionalNodeInformation);

    void deleteOperation(GUID guid);

    FunctionalNodeInformation SelectOperation(GUID guid);

    void UpdateOperation(FunctionalNodeInformation functionalNodeInformation);
}
