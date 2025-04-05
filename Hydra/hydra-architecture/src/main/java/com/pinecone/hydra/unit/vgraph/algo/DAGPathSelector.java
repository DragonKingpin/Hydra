package com.pinecone.hydra.unit.vgraph.algo;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface DAGPathSelector extends Pinenut {
    GUID searchId(String[] parts );

    GUID searchId(String[] parts, @Nullable String[] lpResolvedPath );

    GUID searchId(List<String > resolvedParts );


    GUID searchId(GUID parentId, String[] parts );

    GUID searchId(GUID parentId, String[] parts, @Nullable String[] lpResolvedPath );

    GUID searchId(GUID parentId, List<String > resolvedParts );

    boolean contains( GUID handleNode, GUID nodeGuid);
}
