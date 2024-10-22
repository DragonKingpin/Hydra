package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;

public interface CommonDataManipulator extends Pinenut {
    //NodeMetadata节点的CRUD
    void insert(ServiceFamilyNode node);

    void remove(GUID guid);

    ServiceFamilyNode getNodeCommonData(GUID guid);

    void update(ServiceFamilyNode node);
}
