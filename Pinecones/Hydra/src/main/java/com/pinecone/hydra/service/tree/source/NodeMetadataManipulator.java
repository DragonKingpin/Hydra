package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeMetadata;

public interface NodeMetadataManipulator {
    //NodeMetadata节点的CRUD
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    void deleteNodeMetadata(GUID UUID);
    GenericNodeMetadata selectNodeMetadata(GUID UUID);
    void updateNodeMetadata(GenericNodeMetadata nodeMetadata);
}
