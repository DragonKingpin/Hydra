package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.task.kom.TaskFamilyNode;

public interface JobElement extends FolderElement, TaskFamilyNode {
    String getDeploymentMethod();

    void setDeploymentMethod(String deploymentMethod);

    @Override
    default JobElement evinceJobElement() {
        return this;
    }
}
