package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;
import java.util.List;

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

    NodeCommonData getNodeCommonData();

    void setNodeCommonData( NodeCommonData nodeCommonData );


    //List<RegistryTreeNode > listItem();

    //put ( String key, RegistryTreeNode val )
//    void put             ( String key, Object val );
//    void remove  ( String key );
//    void update  ( XXXXX xxxx );
//    XXXXX get ( String key );
//    boolean containsKey  ( String key );
//
//
//    int size();
//    boolean isEmpty();
//    List<Object > values();
//    Set<String > keySet();
//    Set<Property > entrySet();
}
