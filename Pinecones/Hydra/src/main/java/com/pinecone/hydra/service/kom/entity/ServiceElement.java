package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

import java.time.LocalDateTime;

public interface ServiceElement extends ServiceTreeNode, ServiceFamilyNode {
    long getEnumId();
    void setEnumId(long id);

    @Override
    default ServiceElement evinceTreeNode() {
        return this;
    }

    GUID getGuid();
    void setGuid(GUID guid);

    GUID getMetaGuid();
    void setMetaGuid( GUID metaGuid );

    String getName();
    void setName(String name);

    GUIDDistributedTrieNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode);

    String getPath();
    void setPath(String path);

    String getType();
    void setType(String type);

    String getAlias();
    void setAlias(String alias);

    String getResourceType();
    void setResourceType(String resourceType);

    String getServiceType();
    void setServiceType(String serviceType);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);

}