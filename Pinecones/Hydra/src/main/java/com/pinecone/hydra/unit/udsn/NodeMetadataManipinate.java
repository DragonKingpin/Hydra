package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericNodeMetadata;

public interface NodeMetadataManipinate {
    //NodeMetadata节点的CRUD
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    void deleteNodeMetadata(GUID UUID);
    GenericNodeMetadata selectNodeMetadata(GUID UUID);
    void updateNodeMetadata(GenericNodeMetadata nodeMetadata);
}
