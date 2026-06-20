package com.pinecone.hydra.storage.mfs;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UFile extends Pinenut {
    String getName();

    String getPath();

    Number size();
}
