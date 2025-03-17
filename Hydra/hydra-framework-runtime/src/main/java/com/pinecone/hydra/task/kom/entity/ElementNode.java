package com.pinecone.hydra.task.kom.entity;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.task.kom.TaskFamilyNode;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public interface ElementNode extends ServiceTreeNode, TaskFamilyNode {
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

    void setDistributedTreeNode(GUIDImperialTrieNode distributedTreeNode);

    JSONObject toJSONObject();

    @Override
    default ElementNode evinceElementNode(){
        return this;
    }
}
