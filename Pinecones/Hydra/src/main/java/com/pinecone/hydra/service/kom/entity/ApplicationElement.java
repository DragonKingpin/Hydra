package com.pinecone.hydra.service.kom.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface ApplicationElement extends ServiceTreeNode, ServiceFamilyNode {
    long getEnumId();
    void setEnumId(long id);

    @Override
    default ApplicationElement evinceTreeNode() {
        return this;
    }

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUIDDistributedTrieNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode);

    //GenericApplicationNodeMeta getApplicationNodeMeta();
    //void setApplicationNodeMeta(GenericApplicationNodeMeta applicationNodeMeta);



    GUID getMetaGuid();
    void setMetaGuid( GUID guid );

    String getPath();
    void setPath(String path);

    String getType();
    void setType(String type);

    String getAlias();
    void setAlias(String alias);

    String getResourceType();
    void setResourceType(String resourceType);

    String getDeploymentMethod();
    void setDeploymentMethod(String deploymentMethod);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
}
