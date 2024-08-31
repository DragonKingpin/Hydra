package com.pinecone.hydra.service.tree.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface MetaNodeWideEntity extends Pinenut {
    GUID getParentGUIDs();
    GUID getGuid();
}
