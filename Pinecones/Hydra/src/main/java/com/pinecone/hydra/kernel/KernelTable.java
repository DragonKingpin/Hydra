package com.pinecone.hydra.kernel;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.kernel.entity.KernelObject;
import com.pinecone.hydra.kernel.entity.KernelObjectMeta;

import java.util.List;

public interface KernelTable {
    KernelObjectMeta getKOByGuid(GUID guid);
    List<KernelObject> ListKOByName(String name);
    List<KernelObject> ListKOByGuid(GUID guid);

}
