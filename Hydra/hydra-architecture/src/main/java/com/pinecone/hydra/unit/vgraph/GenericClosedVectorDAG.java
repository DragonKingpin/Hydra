package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;

import java.util.List;

public class GenericClosedVectorDAG extends ArchVectorDAG implements ClosedRangedDAG{
    protected List<GUID>            mSinkNodeGuids;

    public GenericClosedVectorDAG(@Nullable Layer affliatedLayer, List<GUID> sourceNodeGuids, List<GUID> sinkNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        super( affliatedLayer, sourceNodeGuids, masterManipulator, vectorGraphConfig );
    }

    // Temporary Graph
    public GenericClosedVectorDAG( @Nullable GUID layerAffiDAGGuid, List<GUID> sourceNodeGuids, List<GUID> sinkNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this( (Layer) null, sourceNodeGuids, sinkNodeGuids, masterManipulator, vectorGraphConfig );

        if ( layerAffiDAGGuid == null ) {
            layerAffiDAGGuid = this.mGuidAllocator.nextGUID();
        }
        this.mLayerAffiDAGGuid = layerAffiDAGGuid;
    }
    @Override
    public List<GUID> getSinkGuids() {
        return this.mSinkNodeGuids;
    }
}
