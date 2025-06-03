package com.pinecone.hydra.unit.vgraph.layer.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerHandleManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;

import java.util.ArrayList;
import java.util.List;

public class LayerOperator extends ArchLayerComponentOperator implements LayerComponentOperator{
    protected LayerManipulator          mLayerManipulator;

    protected LayerHandleManipulator    mLayerHandleManipulator;

    public LayerOperator( LayerComponentOperatorFactory factory ) {
        this( factory.getMasterManipulator(), factory.getLayerManager() );
        this.mFactory = factory;
    }

    public LayerOperator(LayerMasterManipulator layerMasterManipulator, LayerInstrument layerInstrument) {
        super(layerMasterManipulator, layerInstrument);
        this.mLayerManipulator = this.mLayerMasterManipulator.getLayerManipulator();
        this.mLayerHandleManipulator = this.mLayerMasterManipulator.getLayerHandleManipulator();
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GUID guid = this.mGuidAllocator.nextGUID();
        AtlasLayer atlasLayer = (AtlasLayer) treeNode;
        atlasLayer.setGuid( guid );
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize(atlasLayer);

        this.mImperialTree.insert(imperialTreeNode);

        LayerGraphHandle layerGraphHandle = new LayerGraphHandle();
        layerGraphHandle.setGuid(atlasLayer.getGuid());
        layerGraphHandle.setName(atlasLayer.getName());
        layerGraphHandle.setUpdateTime(atlasLayer.getUpdateTime());
        layerGraphHandle.setCreateTime(atlasLayer.getCreateTime());

        this.mLayerManipulator.insertLayer( layerGraphHandle );

        if( atlasLayer.getSourceGuids() != null ) {
            this.mLayerHandleManipulator.batchInsertSourceNodes( layerGraphHandle.getGuid(), atlasLayer.getSourceGuids() );
        }

        if( atlasLayer.getSinkGuids() != null ) {
            this.mLayerHandleManipulator.batchInsertSinkNodes( layerGraphHandle.getGuid(), atlasLayer.getSinkGuids() );
        }


        return guid;
    }

    @Override
    public void purge(GUID guid) {
        List<GUIDImperialTrieNode> children = this.mImperialTree.getChildren(guid);
        for( GUIDImperialTrieNode node : children ) {
            TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{this.getClass()}, this );
            LayerComponentOperator operator = this.mFactory.getOperator(this.getLayerNodeMetaType(newInstance));
            operator.purge( node.getGuid() );
        }
        this.removeNode( guid );
    }

    @Override
    public TreeNode get(GUID guid) {
        Layer layer = this.mLayerManipulator.queryLayer(guid);
        List<GUID> sourceNodeGuids = this.mLayerHandleManipulator.fetchSourceNodes(layer.getGuid());
        List<GUID> sinkNodeGuids = this.mLayerHandleManipulator.fetchSinkNodes(layer.getGuid());
        layer.setSourceGuids( sourceNodeGuids );
        layer.setSinkGuids( sinkNodeGuids );
        return layer;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getAsRootDepth(GUID guid) {
        return null;
    }

    @Override
    public void update(TreeNode treeNode) {

    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    private void removeNode( GUID guid ) {
        this.mImperialTree.purge( guid );
        this.mImperialTree.removeCachePath( guid );
        this.mLayerManipulator.remove( guid );
    }
}
