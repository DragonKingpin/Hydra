package com.pinecone.hydra.business.source.tag;

import java.util.List;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.tag.NodeTag;

public interface NodeTagManipulator {
    List<NodeTag> fetchByNodeGuids( List<GUID> nodeGuids );
    List<String> fetchCandidates( String pattern, int limit );
    List<GUID> findNodeGuids( String tag, int offset, int limit );
    long countNodes( String tag );
    void insert( GUID nodeGuid, String tag );
    void removeTag( GUID nodeGuid, String tag );
    void removeByNodeGuid( GUID nodeGuid );
}
