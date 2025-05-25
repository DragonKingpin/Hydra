package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.hydra.deploy.kom.DeployFamilyNode;

public interface ClusterElement extends FolderElement, DeployFamilyNode {
    @Override
    default ClusterElement evinceClusterElement() {
        return this;
    }

    String getType();

    void setType( String type );
}
