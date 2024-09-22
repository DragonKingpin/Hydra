package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.EvinceTreeNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.time.LocalDateTime;

public interface NamespaceNode extends EvinceTreeNode {
    int getEnumId();
    void setEnumId(int enumId);
    GUID getGuid();
    void setGuid(GUID guid);
    String getName();
    void setName(String name);
    LocalDateTime getCreateTime();

    @Override
    default NamespaceNode evinceNamespaceTreeNode() {
        return this;
    }

    void setCreateTime(LocalDateTime createTime);
    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
    GenericNamespaceNodeMeta getNamespaceNodeMeta();
    void setNamespaceNodeMeta(GenericNamespaceNodeMeta namespaceNodeMeta);
    GenericNodeCommonData getNodeCommonData();
    void setNodeCommonData(GenericNodeCommonData nodeCommonData);
}
