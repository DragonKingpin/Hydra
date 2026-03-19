package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.task.kom.TaskFamilyNode;

public interface AppElement extends FolderElement, TaskFamilyNode {
    @Override
    default AppElement evinceAppElement() {
        return this;
    }

    String getType();

    void setType( String type );
}
