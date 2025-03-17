package com.pinecone.hydra.task.kom.entity;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.task.kom.source.TaskFamilyNode;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface ElementNode extends TaskTreeNode, TaskFamilyNode {
    default Namespace evinceNamespace() {
        return null;
    }

    default TaskElement evinceServiceElement() {
        return null;
    }

    GUIDImperialTrieNode getDistributedTreeNode();

    void setDistributedTreeNode( GUIDImperialTrieNode distributedTreeNode );

    JSONObject toJSONObject();

    @Override
    default ElementNode evinceElementNode(){
        return this;
    }
}
