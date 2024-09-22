package com.pinecone.hydra.unit.udtt.entity;

public interface EvinceTreeNode extends TreeNode{
    default EvinceTreeNode evinceRegistryTreeNode(){
        return null;
    }
    default EvinceTreeNode evinceNamespaceTreeNode(){
        return null;
    }

}
