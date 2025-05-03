package com.pinecone.hydra.atlas.graph.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface VgraphTaskMappingManipulator extends Pinenut {
    void insert( GUID taskGuid, GUID vgraphNodeGuid );

    GUID queryVgraphNodeGuid( GUID taskGuid );

    GUID queryTaskGuid( GUID vgraphNodeGuid );

    void remove( GUID taskGuid, GUID vgraphNodeGuid );

    void removeByVgraphNodeGuid( GUID vgraphNodeGuid );

    void removeByTaskGuid( GUID taskGuid );
}
