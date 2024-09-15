package com.pinecone.hydra.unit.udsn.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.name.Name;

public interface TreeNode extends Pinenut {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }
}
