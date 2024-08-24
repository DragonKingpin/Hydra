package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface MetaNodeOperator extends Pinenut {
    GUID insert(FunctionalNodeMeta functionalNodeMeta);

    void remove(GUID guid);

    FunctionalNodeMeta get(GUID guid);

    void update(FunctionalNodeMeta functionalNodeMeta);
}
