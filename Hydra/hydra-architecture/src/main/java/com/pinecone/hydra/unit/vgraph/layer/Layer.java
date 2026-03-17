package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;
import java.util.List;

public interface Layer extends LayerTreeNode {
    void setName( String name );

    void setGuid( GUID guid );

    void setParentGuid( GUID parentGuid );

    GUID getParentGuid();

    List<GUID> getSourceGuids();

    void setSourceGuids(List<GUID> handleGuids );

    List<GUID> getSinkGuids();

    void setSinkGuids(List<GUID> endGuids );

    GUID addSourceeGuid(GUID handleGuid );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime startTime );
}
