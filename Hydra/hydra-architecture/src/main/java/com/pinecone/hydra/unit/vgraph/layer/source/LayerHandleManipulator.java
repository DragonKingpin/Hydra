package com.pinecone.hydra.unit.vgraph.layer.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface LayerHandleManipulator extends Pinenut {
    void insertSourceNode( GUID layerGuid, GUID handleGuid );

    void insertSinkNode( GUID layerGuid, GUID handleGuid );

    void batchInsertSourceNodes( GUID layerGuid, List<GUID> handleGuids );

    void batchInsertSinkNodes( GUID layerGuid, List<GUID> handleGuids );

    List<GUID> fetchSourceNodes( GUID layerGuid );

    List<GUID> fetchSinkNodes( GUID layerGuid );

    long countSourceNode( GUID layerGuid );

    List<GUID> fetchSourceGuidsByTaskPriority(GUID layerGuid, long offset, long limit);
}
