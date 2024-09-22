package com.pinecone.hydra.registry.entity;

import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface RegistryTreeNode extends TreeNode {

    default ConfigNode evinceConfigNode(){
        return null;
    }

    default NamespaceNode evinceNamespaceNode(){
        return null;
    }

    default PropertyConfigNode evincePropertyConfig() {
        return null;
    }

    default TextConfigNode evinceTextConfigNode() {
        return null;
    }

    //DistributedRegistry getRegistry();
}
