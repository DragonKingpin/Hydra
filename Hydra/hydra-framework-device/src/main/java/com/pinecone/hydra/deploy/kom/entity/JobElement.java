package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.hydra.deploy.kom.DeployFamilyNode;

public interface JobElement extends FolderElement, DeployFamilyNode {
    @Override
    default JobElement evinceJobElement() {
        return this;
    }

    String getType();

    void setType(String type);
}
