package com.pinecone.hydra.unit.vgraph.layer.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerManager;
import com.pinecone.hydra.unit.vgraph.layer.LayerTreeNode;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;

import java.time.LocalDateTime;

public abstract class ArchLayerComponentOperator implements LayerComponentOperator {
    protected LayerManager                      mLayerManager;

    protected LayerComponentOperatorFactory     mFactory;

    protected ImperialTree                      mImperialTree;

    protected LayerMasterManipulator            mLayerMasterManipulator;

    protected GuidAllocator                     mGuidAllocator;


    public ArchLayerComponentOperator( LayerMasterManipulator layerMasterManipulator, LayerManager layerManager ) {
        this.mImperialTree = layerManager.getMasterTrieTree();
        this.mLayerManager = layerManager;
        this.mLayerMasterManipulator = layerMasterManipulator;
        this.mGuidAllocator  = layerManager.getGuidAllocator();
    }

    protected ImperialTreeNode affirmPreinsertionInitialize( LayerTreeNode treeNode ) {
        GUID guid = treeNode.getGuid();
        treeNode.setUpdateTime( LocalDateTime.now() );
        GUIDImperialTrieNode imperialTrieNode = new GUIDImperialTrieNode();
        imperialTrieNode.setGuid( guid );
        imperialTrieNode.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );

        return imperialTrieNode;
    }

    public LayerComponentOperatorFactory getLayerComponentOperatorFactory() {
        return this.mFactory;
    }

    protected String getLayerNodeMetaType( TreeNode treeNode ) {
        return treeNode.className().replace("Atlas","");
    }
}
