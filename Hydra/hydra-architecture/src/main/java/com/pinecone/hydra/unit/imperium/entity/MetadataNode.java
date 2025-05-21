package com.pinecone.hydra.unit.imperium.entity;

public interface MetadataNode extends EntityNode {

    default MetadataNode evinceMetadataNode() {
        return this;
    }

}
