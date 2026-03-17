package com.pinecone.slime.meta;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TableIndexMeta extends Pinenut {

    long getMaxId();

    long getMinId();

}
