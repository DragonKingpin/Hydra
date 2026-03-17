package com.pinecone.hydra.unit.vgraph.layer.source;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;
import com.pinecone.slime.meta.TableIndexMeta;

import java.util.List;

public interface LayerManipulator extends GUIDNameManipulator {

    void insertLayer(LayerGraphHandle layer );

    void remove( GUID guid );

    Layer queryLayer( GUID guid );

    List<Layer> fetchLayer( List<GUID> guids );

    List<GUID > getGuidsByName( String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );

    void batchInsertLayer( List<LayerGraphHandle> list );


    // anyNode: true: ignore parentGuid, false: filter by parentGuid
    List<Layer> fetchLayerPage( long offset, long limit, boolean anyNode, @Nullable GUID parentGuid );

    List<Layer> fetchLayerPageById( long idStart, long idEnd, boolean anyNode, @Nullable GUID parentGuid );

    TableIndexMeta selectLayerIndexMeta( boolean anyNode, @Nullable GUID parentGuid );

    long countLayer( boolean anyNode, @Nullable GUID parentGuid );

}
