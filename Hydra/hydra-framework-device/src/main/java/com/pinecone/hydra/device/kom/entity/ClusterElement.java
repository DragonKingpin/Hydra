package com.pinecone.hydra.device.kom.entity;

import com.pinecone.hydra.device.kom.DeviceFamilyNode;

public interface ClusterElement extends FolderElement, DeviceFamilyNode {
    @Override
    default ClusterElement evinceClusterElement() {
        return this;
    }

    String getType();

    void setType( String type );
}
