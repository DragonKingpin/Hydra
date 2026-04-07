package com.walnut.odin.atlas.mapper;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface QueueStratumManipulator extends Pinenut {
    String querySegmentName( GUID vgraphGuid, short stratumId, short runtimePriority );

    Integer countStratum( GUID vgraphGuid );

    Integer countPriority( GUID vgraphGuid, short stratumId  );

    void put( GUID vgraphGuid, short stratumId, short runtimePriority, String segmentName );
}
