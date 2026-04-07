package com.walnut.odin.atlas.graph;

import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.util.Assert;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.ArchAtlasInstrument;
import com.pinecone.hydra.unit.vgraph.MagnitudeVectorDAG;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.VectorGraphConfig;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.slime.meta.TableIndexMeta;

import com.walnut.odin.atlas.advance.GenericGraphStratumTape;
import com.walnut.odin.atlas.advance.GraphStratumTape;
import com.walnut.odin.atlas.graph.entity.TaskGraphNode;
import com.walnut.odin.atlas.mapper.QueueStratumManipulator;
import com.walnut.odin.atlas.mapper.RunAtlasMasterManipulator;
import com.walnut.odin.atlas.mapper.TaskGraphManipulator;

import java.util.List;

public class UniformRuntimeAtlas extends ArchAtlasInstrument implements RuntimeAtlasInstrument {

    private TaskInstrument                       mTaskInstrument;

    private RunAtlasMasterManipulator            mRuntimeMasterManipulator;

    private VectorGraphMasterManipulator         mVectorGraphMasterManipulator;

    private TaskGraphManipulator                 mTaskGraphManipulator;

    private QueueStratumManipulator              mQueueStratumManipulator;

    protected void init( TaskInstrument taskInstrument ) {
        this.mTaskInstrument                   = taskInstrument;
        this.mRuntimeMasterManipulator         = (RunAtlasMasterManipulator) this.mAtlasMasterManipulator;
        this.mQueueStratumManipulator          = this.mRuntimeMasterManipulator.getQueueStratumManipulator();
        this.mVectorGraphMasterManipulator     = this.mRuntimeMasterManipulator.getVectorGraphMasterManipulator();
        this.mTaskGraphManipulator             = (TaskGraphManipulator) this.mVectorGraphMasterManipulator.getVectorGraphManipulator();
    }

    public UniformRuntimeAtlas(
            TaskInstrument taskInstrument, LayerInstrument layerInstrument, AtlasMappingDriver driver, VectorGraphConfig config
    ) {
        super( driver, config, layerInstrument );
        this.init( taskInstrument );
    }

    public UniformRuntimeAtlas( AtlasMappingDriver driver, TaskInstrument taskInstrument, LayerInstrument layerInstrument ) {
        super( driver, layerInstrument );
        this.init( taskInstrument );
    }

    @Override
    public TaskInstrument taskInstrument() {
        return this.mTaskInstrument;
    }



    @Override
    public GUID put( GraphNode graphNode ) {
        return super.put(graphNode);
    }

    @Override
    public void remove( GUID guid ) {
        super.remove(guid);
    }

    public TaskGraphNode query( GUID guid ) {
        return (TaskGraphNode) super.get(guid);
    }

    @Override
    public GraphNode queryGraphNodeByTaskGuid( GUID taskGuid ) {
        TaskGraphNode taskGraphNode = this.mTaskGraphManipulator.getNodeByTaskGuid( taskGuid );
        GUID guid = taskGraphNode.getId();
        return this.query(guid);
    }

    @Override
    public TaskElement queryTaskElementByGuid( GUID graphNodeGuid ) {
        GUID guid = this.mTaskGraphManipulator.queryTaskGuidByNodeId( graphNodeGuid );
        TaskTreeNode taskTreeNode = (TaskTreeNode) this.mTaskInstrument.get( guid );
        ElementNode elementNode = taskTreeNode.evinceElementNode();
        if ( elementNode != null ) {
            return elementNode.evinceTaskElement();
        }
        return null;
    }

    @Override
    public GraphStratumTape tapedGraphStratumAdvancer( VectorDAG vectorDAG, KOIMappingDriver driver ) {
        return new GenericGraphStratumTape( this, vectorDAG, driver );
    }

    @Override
    public String querySegmentName( GUID vgraphGuid, short stratumId, short runtimePriority ) {
        return this.mQueueStratumManipulator.querySegmentName( vgraphGuid, stratumId, runtimePriority );
    }

    @Override
    public int countStratum( GUID vgraphGuid ) {
        Integer i = this.mQueueStratumManipulator.countStratum( vgraphGuid );
        Assert.notNull( i );
        return i;
    }

    @Override
    public int countPriority( GUID vgraphGuid, short stratumId ) {
        Integer i = this.mQueueStratumManipulator.countPriority( vgraphGuid, stratumId );
        Assert.notNull( i );
        return i;
    }

    @Override
    public void putStratumMeta( GUID vgraphGuid, short stratumId, short runtimePriority, String segmentName ) {
        this.mQueueStratumManipulator.put( vgraphGuid, stratumId, runtimePriority, segmentName );
    }

    @Override
    public VectorDAG toVectorDAG( Layer layer ) {
        return new MagnitudeVectorDAG(
                layer,
                this.mVectorGraphMasterManipulator,
                this.mVectorGraphConfig
        );
    }

    @Override
    public VectorDAG getByLayerGuid( GUID layerGuid ) {
        TreeNode treeNode = this.mLayerInstrument.get( layerGuid );
        if ( !( treeNode instanceof Layer ) ) {
            return null;
        }
        Layer layer = (Layer) treeNode;
        return this.toVectorDAG( layer );
    }

    @Override
    public VectorDAG queryByPath( String path ) {
        EntityNode entityNode = this.mLayerInstrument.queryNode( path );
        if ( !( entityNode instanceof Layer ) ) {
            return null;
        }
        Layer layer = (Layer) entityNode;
        return this.toVectorDAG( layer );
    }

    @Override
    public List<GUID> fetchParentIds(GUID graphNodeGuid) {
        return  this.mTaskGraphManipulator.fetchParentIds( graphNodeGuid );
    }

    @Override
    public void addChild( GUID parentGuid, GUID childGuid ) {
        this.mVectorGraphManipulator.addChild( parentGuid,childGuid );
    }






    @Unsafe( "TestOnly" )
    @Override
    public List<GraphNode> fetchIsolatedNodesAll() {
        TableIndexMeta meta = this.getIsolatedNodeIndexMeta();
        return this.fetchIsolatedNodesById( meta.getMinId(), meta.getMaxId() );
    }

    @Override
    public List<GraphNode> fetchIsolatedNodes( long offset, long limit ) {
        return this.mTaskGraphManipulator.fetchIsolatedNodes( offset, limit );
    }

    @Override
    public List<GraphNode> fetchIsolatedNodesById( long idStart, long idEnd ) {
        return this.mTaskGraphManipulator.fetchIsolatedNodesById( idStart, idEnd );
    }

    @Override
    public TableIndexMeta getIsolatedNodeIndexMeta() {
        return this.mTaskGraphManipulator.selectIsolatedNodeIndexMeta();
    }

    @Override
    public long queryMaxIsolatedNodePage( long limit ) {
        if ( limit <= 0 ) {
            throw new IllegalArgumentException( "Limit must be greater than zero." );
        }

        long nTotal = this.mTaskGraphManipulator.countIsolatedNodes();
        if ( nTotal == 0 ) {
            return 0;
        }

        long nPage = nTotal / limit;
        if ( nTotal % limit != 0 ) {
            nPage++;
        }

        return nPage;
    }

}
