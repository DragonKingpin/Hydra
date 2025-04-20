package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface LayerMapper extends LayerManipulator {

    @Insert("INSERT INTO hydra_atlas_layer_layers (layer_guid, graph_node_guid, layer_name, update_time, create_time) VALUES (#{guid}, #{}))")
    void insertStartLayer( LayerGraphHandle layer );

    void insertLayer(GUID parentGuid, Layer layer);

    void remove( GUID guid );

    Layer queryLayer( GUID guid );

    List<GUID > getGuidsByName(String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );
}
