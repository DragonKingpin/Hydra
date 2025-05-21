package com.pinecone.hydra.unit.imperium.entity;

public interface SkeletonNode extends EntityNode {

    default SkeletonNode evinceSkeletonNode(){
        return this;
    }

}
