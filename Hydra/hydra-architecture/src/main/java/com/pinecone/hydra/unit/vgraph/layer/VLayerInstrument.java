package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.Hydrogen;
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
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.layer.operator.AtlasLayerComponentOperatorFactory;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerHandleManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerMasterManipulator;
import com.pinecone.hydra.unit.vgraph.layer.source.NamespaceManipulator;
import com.pinecone.slime.meta.TableIndexMeta;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VLayerInstrument extends ArchKOMTree implements LayerInstrument {
    protected LayerMasterManipulator    mLayerMasterManipulator;

    protected LayerManipulator          mLayerManipulator;

    protected NamespaceManipulator      mNamespaceManipulator;

    protected LayerHandleManipulator    mLayerHandleManipulator;

    public VLayerInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerInstrument parent, String name, String superiorPathScope, @Nullable GuidAllocator guidAllocator ) {
        super( superiorProcess, masterManipulator, LayerInstrument.LayerConfig, parent, name, superiorPathScope, guidAllocator );
        this.mLayerMasterManipulator    = (LayerMasterManipulator) masterManipulator;
        this.pathResolver               = new KOPathResolver( this.kernelObjectConfig );

        this.operatorFactory            = new AtlasLayerComponentOperatorFactory( this, (LayerMasterManipulator) masterManipulator);
        this.mLayerManipulator          = this.mLayerMasterManipulator.getLayerManipulator();
        this.mNamespaceManipulator      = this.mLayerMasterManipulator.getNamespaceManipulator();
        this.mLayerHandleManipulator    = this.mLayerMasterManipulator.getLayerHandleManipulator();

        this.pathSelector = new SimplePathSelector(
                this.pathResolver, this.imperialTree, this.mNamespaceManipulator,new GUIDNameManipulator[]{ this.mLayerManipulator }
        );
    }

    public VLayerInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, LayerInstrument parent, String name ) {
        this( superiorProcess, masterManipulator, parent, name, CascadeInstrument.EmptySuperiorPathScope, new GuidAllocator128V7());
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
    public Hydrogen getSystem() {
        return this.hydrogen;
    }

    @Override
    public LayerConfig getConfig() {
        return (LayerConfig) this.kernelObjectConfig;
    }

    @Override
    public GUID put( TreeNode treeNode ) {
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
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{this.getClass()}, null );
        return this.operatorFactory.getOperator( this.getLayerMetaType( newInstance ) );
    }

    @Override
    public Layer get( GUID guid ) {
        TreeNodeOperator operator = this.getOperatorByGuid( guid );
        if( operator == null ) {
            return null;
        }
        return (Layer) operator.get( guid );
    }

    @Override
    public void addChild( GUID parentGuid, GUID childGuid ) {
        this.imperialTree.affirmOwnedNode(childGuid, parentGuid);
    }

    @Override
    public void update( TreeNode treeNode ) {

    }

    @Override
    public List<Layer> splitGraphLayer( VectorDAG vectorDAG ) {
        Layer layer = this.mLayerManipulator.queryLayer( vectorDAG.getAffiliateLayerGuid() );
        List<TreeNode> children = this.getChildren( layer.getGuid() );
        List<GUID> collect = children.stream().map(TreeNode::getGuid).collect(Collectors.toList());
        List<Layer> layers = this.mLayerManipulator.fetchLayer(collect);
        return layers;
    }

    @Override
    public long countSourceNode( GUID layerGuid ) {
        return this.mLayerHandleManipulator.countSourceNode( layerGuid );
    }

    @Override
    public List<GUID> fetchSourceGuidsByTaskPriority( GUID layerGuid, long offset, long limit ) {
        return this.mLayerHandleManipulator.fetchSourceGuidsByTaskPriority( layerGuid, offset, limit );
    }





    @Override
    public List<Layer> fetchLayers( long offset, long limit, boolean anyNode, @Nullable GUID parentGuid ) {
        return this.mLayerManipulator.fetchLayerPage( offset, limit, anyNode, parentGuid );
    }

    @Unsafe( "TestOnly" )
    @Override
    public List<Layer> fetchLayersAll() {
        TableIndexMeta meta = this.getLayerIndexMeta();
        return this.fetchLayersById( meta.getMinId(), meta.getMaxId() );
    }

    @Override
    public List<Layer> fetchLayersById( long idStart, long idEnd, boolean anyNode, @Nullable GUID parentGuid ) {
        return this.mLayerManipulator.fetchLayerPageById( idStart, idEnd, anyNode, parentGuid );
    }

    @Override
    public TableIndexMeta getLayerIndexMeta( boolean anyNode, @Nullable GUID parentGuid ) {
        return this.mLayerManipulator.selectLayerIndexMeta( anyNode, parentGuid );
    }

    @Override
    public long queryMaxLayerPage( long limit, boolean anyNode, @Nullable GUID parentGuid ) {
        if ( limit <= 0 ) {
            throw new IllegalArgumentException( "Limit must be greater than zero." );
        }

        long nTotal = this.mLayerManipulator.countLayer( anyNode, parentGuid );
        if ( nTotal == 0 ) {
            return 0;
        }

        long nPage = nTotal / limit;
        if ( nTotal % limit != 0 ) {
            ++nPage;
        }

        return nPage;
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
