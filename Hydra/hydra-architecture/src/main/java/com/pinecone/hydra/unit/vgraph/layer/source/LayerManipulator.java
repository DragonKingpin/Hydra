package com.pinecone.hydra.unit.vgraph.layer.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;

import java.util.List;

public interface LayerManipulator extends GUIDNameManipulator {

    void insertLayer(LayerGraphHandle layer );

    void remove( GUID guid );

    Layer queryLayer( GUID guid );

    List<Layer> fetchLayer( List<GUID> guids );

    List<GUID > getGuidsByName(String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );

    void batchInsertLayer(List<LayerGraphHandle> list);
}
