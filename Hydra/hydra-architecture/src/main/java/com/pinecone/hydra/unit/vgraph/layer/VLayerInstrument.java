package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.system.ko.kom.SimplePathSelector;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.vgraph.layer.operator.AtlasLayerComponentOperatorFactory;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.NamespaceManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.time.LocalDateTime;

public class VLayerInstrument extends ArchKOMTree implements LayerInstrument {
    protected LayerMasterManipulator    mLayerMasterManipulator;

    protected LayerManipulator          mLayerManipulator;

    protected NamespaceManipulator      mNamespaceManipulator;

    public VLayerInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerInstrument parent, String name, String superiorPathScope ) {
        super( superiorProcess, masterManipulator, LayerInstrument.LayerConfig, parent, name, superiorPathScope );
        this.mLayerMasterManipulator    = (LayerMasterManipulator) masterManipulator;
        this.pathResolver               = new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator              = new GenericGuidAllocator();

        this.operatorFactory            = new AtlasLayerComponentOperatorFactory( this, (LayerMasterManipulator) masterManipulator);
        this.mLayerManipulator          = this.mLayerMasterManipulator.getLayerManipulator();
        this.mNamespaceManipulator      = this.mLayerMasterManipulator.getNamespaceManipulator();

        this.pathSelector = new SimplePathSelector(
                this.pathResolver, this.imperialTree, this.mNamespaceManipulator,new GUIDNameManipulator[]{ this.mLayerManipulator }
        );
    }

    public VLayerInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerInstrument parent, String name ) {
        this( superiorProcess, masterManipulator, parent, name, CascadeInstrument.EmptySuperiorPathScope );
    }

    public VLayerInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, LayerConfig.class.getSimpleName() );
    }

    public VLayerInstrument( KOIMappingDriver driver ) {
        this(driver.getSuperiorProcess(), driver.getMasterManipulator());
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
    public GUID put(TreeNode treeNode ) {
        TreeNodeOperator operator = this.operatorFactory.getOperator( this.getLayerMetaType( treeNode ) );
        return operator.insert( treeNode );
    }

    @Override
    public void remove(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.operatorFactory.getOperator( this.getLayerMetaType( newInstance ) );
        operator.purge( guid );
    }

    protected TreeNodeOperator getOperatorByGuid( GUID guid ) {
        ImperialTreeNode node = this.imperialTree.getNode( guid );
        if ( node == null ){
            return null;
        }
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{this.getClass()}, this );
        return this.operatorFactory.getOperator( this.getLayerMetaType( newInstance ) );
    }

    @Override
    public Layer get(GUID guid) {
        TreeNodeOperator operator = this.getOperatorByGuid( guid );
        if( operator == null ) {
            return null;
        }
        return (Layer) operator.get( guid );
    }

    @Override
    public void addChild(GUID parentGuid, GUID childGuid) {
        this.imperialTree.affirmOwnedNode(childGuid, parentGuid);
    }

    @Override
    public void update( TreeNode treeNode ) {

    }

    protected ImperialTreeNode affirmPreinsertionInitialize(AtlasLayer atlasLayer) {
        GUID guid = atlasLayer.getGuid();
        atlasLayer.setUpdateTime(LocalDateTime.now());
        GUIDImperialTrieNode imperialTrieNode = new GUIDImperialTrieNode();
        imperialTrieNode.setGuid(guid);
        return imperialTrieNode;
    }

    private String getLayerMetaType( TreeNode treeNode ) {
        return treeNode.className().replace( "Atlas", "" );
    }
}
