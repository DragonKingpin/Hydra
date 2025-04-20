package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.system.ko.kom.SimplePathSelector;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.time.LocalDateTime;

public class VLayerManager extends ArchKOMTree implements LayerManager {
    protected LayerMasterManipulator mLayerMasterManipulator;

    protected LayerManipulator       mLayerManipulator;

    public VLayerManager(Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerManager parent, String name, LayerConfig config ) {
        super( superiorProcess, masterManipulator, config, parent, name );
        this.mLayerMasterManipulator = (LayerMasterManipulator) masterManipulator;
        this.pathResolver = new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator = new GenericGuidAllocator();

        this.mLayerManipulator = this.mLayerMasterManipulator.getLayerManipulator();

        this.pathSelector = new SimplePathSelector(
                this.pathResolver, this.imperialTree, this.mLayerManipulator,new GUIDNameManipulator[]{}
        );
    }

    public VLayerManager(Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerConfig config ) {
        this( superiorProcess, masterManipulator, null, LayerConfig.class.getSimpleName(), config );
    }

    public VLayerManager(KOIMappingDriver driver, LayerManager parent, String name, LayerConfig config ) {
        this(driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name, config);
    }

    public VLayerManager(KOIMappingDriver driver, LayerConfig config ) {
        this(driver.getSuperiorProcess(), driver.getMasterManipulator(), config);
    }

    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @Override
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public Hydrarum getHydrarum() {
        return this.hydrarum;
    }

    @Override
    public LayerConfig getConfig() {
        return (LayerConfig) this.kernelObjectConfig;
    }

    @Override
    public GUID put( TreeNode treeNode ) {
        VgraphLayer vgraphLayer = (VgraphLayer) treeNode;
        vgraphLayer.setGuid(this.guidAllocator.nextGUID());
        ImperialTreeNode imperialTreeNode = this.affirmPreinsertionInitialize(vgraphLayer);
        GUID guid = vgraphLayer.getGuid();
        this.imperialTree.insert(imperialTreeNode);
        //this.mLayerManipulator.insertStartLayer(vgraphLayer);
        return guid;
    }

    @Override
    public void remove(GUID guid) {
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.mLayerManipulator.remove( guid );
    }

    @Override
    public Layer get(GUID guid) {
        return this.mLayerManipulator.queryLayer(guid);
    }

    protected ImperialTreeNode affirmPreinsertionInitialize( VgraphLayer vgraphLayer ) {
        GUID guid = vgraphLayer.getGuid();
        vgraphLayer.setUpdateTime(LocalDateTime.now());
        GUIDImperialTrieNode imperialTrieNode = new GUIDImperialTrieNode();
        imperialTrieNode.setGuid(guid);
        return imperialTrieNode;
    }
}
