package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;

public interface NodeMetadataManipulator {
    //NodeMetadata节点的CRUD
    void insertNodeMetadata(GenericNodeCommonData nodeMetadata);
    void deleteNodeMetadata(GUID guid);
    GenericNodeCommonData getNodeMetadata(GUID guid);
    void updateNodeMetadata(GenericNodeCommonData nodeMetadata);
}
