package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface FunctionalNodeOperation extends Pinenut {
    GUID addOperation(FunctionalNodeInformation functionalNodeInformation);
    void deleteOperation(GUID guid);
    FunctionalNodeInformation SelectOperation(GUID guid);
    void UpdateOperation(FunctionalNodeInformation functionalNodeInformation);
}
