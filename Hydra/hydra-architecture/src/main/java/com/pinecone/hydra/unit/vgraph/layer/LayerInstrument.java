package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.slime.meta.TableIndexMeta;

import java.util.List;

public interface LayerInstrument extends KOMInstrument {
    LayerConfig LayerConfig = new VLayerConfig();

    LayerConfig getConfig();

    Hydrogen getSystem();

    void addChild( GUID parentGuid, GUID childGuid );

    void update( TreeNode treeNode );

    List<Layer> splitGraphLayer(VectorDAG vectorDAG );

    long countSourceNode( GUID layerGuid );

    List<GUID> fetchSourceGuidsByTaskPriority( GUID layerGuid,long offset, long limit );




    @Unsafe( "TestOnly" )
    List<Layer> fetchLayersAll();

    List<Layer> fetchLayers( long offset, long limit, boolean anyNode, @Nullable GUID parentGuid );

    default List<Layer> fetchLayers( long offset, long limit ) {
        return this.fetchLayers( offset, limit, true, null );
    }

    List<Layer> fetchLayersById( long idStart, long idEnd, boolean anyNode, @Nullable GUID parentGuid );

    default  List<Layer> fetchLayersById( long idStart, long idEnd ) {
        return this.fetchLayersById( idStart, idEnd, true, null );
    }

    TableIndexMeta getLayerIndexMeta( boolean anyNode, @Nullable GUID parentGuid );

    default TableIndexMeta getLayerIndexMeta() {
        return this.getLayerIndexMeta( true, null );
    }

    long queryMaxLayerPage( long limit, boolean anyNode, @Nullable GUID parentGuid );

    default long queryMaxLayerPage( long limit ) {
        return this.queryMaxLayerPage( limit, true, null );
    }

}
