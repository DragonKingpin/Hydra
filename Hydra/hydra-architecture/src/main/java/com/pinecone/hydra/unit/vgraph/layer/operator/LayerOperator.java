package com.pinecone.hydra.unit.vgraph.layer.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;
import com.pinecone.hydra.unit.vgraph.layer.LayerManager;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;

import java.util.ArrayList;
import java.util.List;

public class LayerOperator extends ArchLayerComponentOperator implements LayerComponentOperator{
    protected LayerManipulator          mLayerManipulator;

    public LayerOperator( LayerComponentOperatorFactory factory ) {
        this( factory.getMasterManipulator(), factory.getLayerManager() );
        this.mFactory = factory;
    }

    public LayerOperator(LayerMasterManipulator layerMasterManipulator, LayerManager layerManager) {
        super(layerMasterManipulator, layerManager);
        this.mLayerManipulator = mLayerMasterManipulator.getLayerManipulator();
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GUID guid = this.mGuidAllocator.nextGUID();
        AtlasLayer atlasLayer = (AtlasLayer) treeNode;
        atlasLayer.setGuid( guid );
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize(atlasLayer);

        this.mImperialTree.insert(imperialTreeNode);
        ArrayList<LayerGraphHandle> layerGraphHandles = new ArrayList<>();
        for( GUID g : atlasLayer.getHandleGuids()) {
            LayerGraphHandle layerGraphHandle = new LayerGraphHandle();
            layerGraphHandle.setGuid(atlasLayer.getGuid());
            layerGraphHandle.setName(atlasLayer.getName());
            layerGraphHandle.setUpdateTime(atlasLayer.getUpdateTime());
            layerGraphHandle.setCreateTime(atlasLayer.getCreateTime());
            layerGraphHandle.setHandleNodeGuid(g);
            layerGraphHandles.add(layerGraphHandle);
        }
        this.mLayerManipulator.batchInsertLayer(layerGraphHandles);
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
        return this.mLayerManipulator.queryLayer(guid);
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getSelf(GUID guid) {
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
