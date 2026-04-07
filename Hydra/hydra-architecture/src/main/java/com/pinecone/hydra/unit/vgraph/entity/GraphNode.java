package com.pinecone.hydra.unit.vgraph.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface GraphNode extends Pinenut {
    long getEnumId();

    void setEnumId(long enumId);

    String getName();

    GUID getId();

    void setId( GUID guid );

    List<GUID> getParentIds();

    void setParentIds( List<GUID> parentIds );

    String getDescription();

    void setDescription( String description );
}
