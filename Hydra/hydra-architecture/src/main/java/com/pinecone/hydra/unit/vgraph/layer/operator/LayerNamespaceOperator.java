package com.pinecone.hydra.unit.vgraph.layer.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerManager;
import com.pinecone.hydra.unit.vgraph.layer.LayerNamespace;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.NamespaceManipulator;

import java.util.List;

public class LayerNamespaceOperator extends ArchLayerComponentOperator implements LayerComponentOperator {
    protected NamespaceManipulator      mNamespaceManipulator;

    public LayerNamespaceOperator( LayerComponentOperatorFactory factory ) {
        this( factory.getMasterManipulator(), factory.getLayerManager() );
        this.mFactory = factory;
    }

    public LayerNamespaceOperator(LayerMasterManipulator layerMasterManipulator, LayerManager layerManager) {
        super(layerMasterManipulator, layerManager);
        this.mNamespaceManipulator = layerMasterManipulator.getNamespaceManipulator();
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GUID guid = this.mGuidAllocator.nextGUID();
        LayerNamespace layerNamespace = (LayerNamespace) treeNode;
        layerNamespace.setGuid( guid );
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize(layerNamespace);

        this.mImperialTree.insert(imperialTreeNode);
        this.mNamespaceManipulator.insert(layerNamespace);
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

        return this.mNamespaceManipulator.query(guid);
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
        this.mNamespaceManipulator.remove( guid );
    }
}
