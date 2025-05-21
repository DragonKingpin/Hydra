package com.pinecone.hydra.unit.imperium.entity;

public interface MetaEntryNode extends EntityNode, MetadataNode {

    default MetaEntryNode evinceEntryNode() {
        return this;
    }

}
