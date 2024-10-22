package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface ServiceTreeNode extends TreeNode {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }
    default ServiceTreeNode evinceTreeNode(){
        return null;
    }
}
