package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface InstanceAtlasAdjacent extends Pinenut {
    GUID getGuid();
    void setGuid( GUID guid );

    GUID getParentGuid();
    void setParentGuid( GUID parentGuid );
}