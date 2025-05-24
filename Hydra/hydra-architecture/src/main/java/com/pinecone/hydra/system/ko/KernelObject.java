package com.pinecone.hydra.system.ko;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface KernelObject extends Pinenut {

    GUID getGuid();

    String objectFunctionName();

    String objectCategoryName();

}
