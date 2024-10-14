package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceTreeNode extends Pinenut {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }
    default ServiceTreeNode evinceTreeNode(){
        return null;
    }
}
