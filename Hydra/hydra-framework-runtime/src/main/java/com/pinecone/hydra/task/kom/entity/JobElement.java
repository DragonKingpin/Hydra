package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.task.kom.TaskFamilyNode;

public interface JobElement extends FolderElement, TaskFamilyNode {
    @Override
    default JobElement evinceJobElement() {
        return this;
    }

    String getType();
    void setType( String type );
}
