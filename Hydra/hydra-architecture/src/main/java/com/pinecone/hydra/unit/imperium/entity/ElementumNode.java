package com.pinecone.hydra.unit.imperium.entity;

public interface ElementumNode extends TreeNode, MetadataNode {

    default ElementumNode evinceElementNode() {
        return this;
    }

}
