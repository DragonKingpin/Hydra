package com.pinecone.slime.chunk.flow.trait;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Leasable extends Pinenut {
    boolean lease();

    void revoke();
}
