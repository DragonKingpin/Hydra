package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.name.Name;

public interface TreeNode extends Pinenut {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }
}
