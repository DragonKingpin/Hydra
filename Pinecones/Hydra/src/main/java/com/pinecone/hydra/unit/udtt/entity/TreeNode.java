package com.pinecone.hydra.unit.udtt.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TreeNode extends Pinenut {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }
}
