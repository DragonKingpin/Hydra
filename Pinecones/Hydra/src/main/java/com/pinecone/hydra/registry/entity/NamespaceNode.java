package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NamespaceNode extends RegistryTreeNode {
    int getEnumId();

    void setEnumId(int enumId);

    GUID getGuid();

    void setGuid(GUID guid);

    String getName();

    void setName( String name );

    LocalDateTime getCreateTime();

    @Override
    default NamespaceNode evinceNamespaceNode() {
        return this;
    }

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    NamespaceNodeMeta getNamespaceNodeMeta();

    void setNamespaceNodeMeta( NamespaceNodeMeta namespaceNodeMeta );

    NodeAttribute getNodeAttribute();

    void setNodeAttribute( NodeAttribute nodeAttribute );

    Map<String, RegistryTreeNode > getChildren();

    List<GUID > getChildrenGuids();

    void setContentGuids( List<GUID> contentGuids );

    List<RegistryTreeNode > listItem();


    void put ( String key, RegistryTreeNode val );

    void remove ( String key );

    DistributedRegistry getRegistry();

    boolean containsKey  ( String key );



    ConfigNode getConfigNode(String key);

    NamespaceNode getNamespaceNode(String key);

    int size();

    boolean isEmpty();

    Set<String > keySet();

    Set<Map.Entry<String,RegistryTreeNode>> entrySet();

}
