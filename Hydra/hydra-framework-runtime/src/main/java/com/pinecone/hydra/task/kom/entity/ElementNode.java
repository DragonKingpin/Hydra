package com.pinecone.hydra.task.kom.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.task.kom.TaskFamilyNode;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface ElementNode extends TaskTreeNode, TaskFamilyNode {

    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode" );

    default Namespace evinceNamespace() {
        return null;
    }

    default JobElement evinceJobElement() {
        return null;
    }

    default TaskElement evinceTaskElement() {
        return null;
    }

    GUIDImperialTrieNode getDistributedTreeNode();

    void setDistributedTreeNode( GUIDImperialTrieNode distributedTreeNode );

    JSONObject toJSONObject();

    @Override
    default ElementNode evinceElementNode(){
        return this;
    }

    GUID getMetaGuid();

    void setMetaGuid( GUID metaGuid );

    String getKomPath();

    String getSystemKernelObjectPath();

    String getName();
    void setName( String name );

    LocalDateTime getCreateTime();
    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();
    void setUpdateTime( LocalDateTime updateTime );

}
