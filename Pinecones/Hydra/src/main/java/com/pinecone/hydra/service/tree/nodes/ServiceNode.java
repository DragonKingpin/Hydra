package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.NodeWideData;
import com.pinecone.hydra.service.tree.ServiceNodeMetadata;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public interface ServiceNode extends NodeWideData {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    DistributedTreeNode getDistributedTreeNode();
    void setDistributedTreeNode(DistributedTreeNode distributedTreeNode);

    ServiceNodeMetadata getServiceNodeMetadata();
    void setServiceNodeMetadata(ServiceNodeMetadata serviceNodeMetadata);
}