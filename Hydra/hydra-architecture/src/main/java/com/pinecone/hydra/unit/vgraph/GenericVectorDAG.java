package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.util.List;

public class GenericVectorDAG extends ArchVectorDAG implements VectorDAG {

    public GenericVectorDAG( @Nullable Layer affliatedLayer, List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        super( affliatedLayer, handleNodeGuids, masterManipulator, vectorGraphConfig );
    }

    // Temporary Graph
    public GenericVectorDAG( @Nullable GUID graphGuid, List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this( (Layer) null, handleNodeGuids, masterManipulator, vectorGraphConfig );

        if ( graphGuid == null ) {
            graphGuid = this.mGuidAllocator.nextGUID();
        }
        this.mLayerAffiDAGGuid = graphGuid;
    }

    // Temporary Graph
    public GenericVectorDAG( List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this( (GUID) null, handleNodeGuids, masterManipulator, vectorGraphConfig );
    }

}
