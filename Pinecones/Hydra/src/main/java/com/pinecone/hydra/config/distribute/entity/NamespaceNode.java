package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.Name;

import java.time.LocalDateTime;

public interface NamespaceNode extends TreeNode {
    int getEnumId();
    void setEnumId(int enumId);
    GUID getGuid();
    void setGuid(GUID guid);
    String getName();
    void setName(String name);
    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);
    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
    GenericNamespaceNodeMeta getNamespaceNodeMeta();
    void setNamespaceNodeMeta(GenericNamespaceNodeMeta namespaceNodeMeta);
    GenericNodeCommonData getNodeCommonData();
    void setNodeCommonData(GenericNodeCommonData nodeCommonData);
}