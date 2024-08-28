package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NodeWideData extends Pinenut {
    GUID getParentGUID();
    GUID getGuid();
}
