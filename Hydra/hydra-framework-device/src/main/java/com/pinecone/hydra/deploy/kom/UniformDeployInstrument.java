package com.pinecone.hydra.deploy.kom;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.deploy.kom.entity.GenericContainerElement;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployInsMapping;
import com.pinecone.hydra.deploy.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.ArchServerElement;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.ServerElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.deploy.kom.source.DeployServiceInsMappingManipulator;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.GenericReparseKOMTreeAddition;
import com.pinecone.hydra.system.ko.kom.MultiFolderPathSelector;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.entity.GenericClusterElement;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.entity.DeployTreeNode;
import com.pinecone.hydra.deploy.kom.operator.GenericElementOperatorFactory;
import com.pinecone.hydra.deploy.kom.source.ClusterNodeManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNodeManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.RegimentedImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;

public class UniformDeployInstrument extends ArchReparseKOMTree implements DeployInstrument {
    //GenericDistributedScopeTree
    protected ImperialTree                          imperialTree;

    protected DeployMasterManipulator               deployMasterManipulator;

    protected DeployNamespaceManipulator            deployNamespaceManipulator;

    protected ClusterNodeManipulator                clusterNodeManipulator;

    protected DeployNodeManipulator                 deployNodeManipulator;

    protected List<GUIDNameManipulator >            folderManipulators;

    protected List<GUIDNameManipulator >            fileManipulators;

    protected PhysicalHostManipulator               physicalHostManipulator;

    protected VirtualMachineManipulator             virtualMachineManipulator;

    protected QuickElementManipulator               quickElementManipulator;

    protected DeployServiceInsMappingManipulator    deployServiceInsMappingManipulator;

    public UniformDeployInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, DeployInstrument parent, String name, @Nullable GuidAllocator guidAllocator ) {
        super( superiorProcess, masterManipulator, DeployInstrument.KERNEL_DEPLOY_CONFIG, parent, name, guidAllocator );

        this.deployMasterManipulator = (DeployMasterManipulator) masterManipulator;
        this.deployNamespaceManipulator = this.deployMasterManipulator.getNamespaceManipulator();
        this.clusterNodeManipulator          = this.deployMasterManipulator.getJobNodeManipulator();
        this.deployNodeManipulator = this.deployMasterManipulator.getDeployNodeManipulator();
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.deployMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.imperialTree                = new RegimentedImperialTree(treeMasterManipulator);
        this.operatorFactory             = new GenericElementOperatorFactory(this,(DeployMasterManipulator) masterManipulator);
        this.physicalHostManipulator     = this.deployMasterManipulator.getPhysicalHostManipulator();
        this.virtualMachineManipulator   = this.deployMasterManipulator.getVirtualMachineManipulator();
        this.pathResolver                = new KOPathResolver( this.kernelObjectConfig );
        this.quickElementManipulator     = this.deployMasterManipulator.getQuickElementManipulator();
        this.deployServiceInsMappingManipulator = this.deployMasterManipulator.getDeployServiceInsMappingManipulator();
        // TODO for customize service tree architecture.
        this.folderManipulators          = new ArrayList<>( List.of( this.deployNamespaceManipulator, this.clusterNodeManipulator) );
        this.fileManipulators            = new ArrayList<>( List.of( this.clusterNodeManipulator, this.physicalHostManipulator, this.virtualMachineManipulator, this.quickElementManipulator) );
        this.pathSelector                = new MultiFolderPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulators.toArray( new GUIDNameManipulator[]{} ), this.fileManipulators.toArray( new GUIDNameManipulator[]{} )
        );

        this.mReparseKOM                 =  new GenericReparseKOMTreeAddition( this );
    }

    public UniformDeployInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, DeployInstrument.class.getSimpleName(), null );
    }

//    public UniformTaskInstrument( Hydrogen hydrogen ) {
//        this.hydrogen = hydrogen;
//    }

    public UniformDeployInstrument( KOIMappingDriver driver ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator()
        );
    }

    public UniformDeployInstrument( KOIMappingDriver driver, DeployInstrument parent, String name ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                parent,
                name,
                null
        );
    }

    protected DeployTreeNode affirmTreeNodeByPath(String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy128();

        DeployTreeNode node = this.queryElement(path);
        if ( node != null ){
            return node;
        }

        DeployTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    ElementNode en = (ElementNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    en.setName( parts[i] );
                    GUID guid = this.put( en );
                    this.affirmOwnedNode( parentGuid, guid );
                    return en;
                }
                else {
                    Namespace namespace = (Namespace) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    namespace.setName( parts[i] );
                    GUID guid = this.put( namespace );
                    if ( i != 0 ){
                        this.affirmOwnedNode( parentGuid, guid );
                        parentGuid = guid;
                    }
                    else {
                        parentGuid = guid;
                    }

                    ret = namespace;
                }
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }

    @Override
    public ClusterElement affirmCluster(String path ) {
        return (ClusterElement) this.affirmTreeNodeByPath( path, GenericClusterElement.class, GenericNamespace.class );
    }

    @Override
    public ServerElement affirmServer(String path) {
        return (ServerElement) this.affirmTreeNodeByPath( path, ArchServerElement.class, GenericNamespace.class );
    }

    @Override
    public QuickElement affirmQuick(String path) {
        return (QuickElement) this.affirmTreeNodeByPath( path, GenericQuickElement.class, GenericNamespace.class );
    }

    @Override
    public VirtualMachineElement affirmVirtualMachine(String path) {
        return (VirtualMachineElement) this.affirmTreeNodeByPath( path, GenericVirtualMachineElement.class, GenericNamespace.class );
    }

    @Override
    public ContainerElement affirmContainerElement(String path) {
        return (ContainerElement) this.affirmTreeNodeByPath( path, GenericContainerElement.class, GenericNamespace.class );

    }

    @Override
    public PhysicalHostElement affirmPhysicalHost(String path) {
        return (PhysicalHostElement) this.affirmTreeNodeByPath( path, GenericPhysicalHostElement.class, GenericNamespace.class );
    }

    @Override
    public ElementNode queryElement( String path ) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return this.get( guid ).evinceElementNode();
        }

        return null;
    }

    @Override
    public Namespace affirmNamespace( String path ) {
        return ( Namespace ) this.affirmTreeNodeByPath( path, null, GenericNamespace.class );
    }


    protected boolean containsChild( GUIDNameManipulator manipulator, GUID parentGuid, String childName ) {
        List<GUID > guids = manipulator.getGuidsByName( childName );
        for( GUID guid : guids ) {
            List<GUID > ps = this.imperialTree.fetchParentGuids( guid );
            if( ps.contains( parentGuid ) ){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsChild( GUID parentGuid, String childName ) {
        for( GUIDNameManipulator manipulator : this.fileManipulators ) {
            if( this.containsChild( manipulator, parentGuid, childName ) ) {
                return true;
            }
        }

        for( GUIDNameManipulator manipulator : this.folderManipulators ) {
            if( this.containsChild( manipulator, parentGuid, childName ) ) {
                return true;
            }
        }
        return false;
    }


    /**
     * Affirm path exist in cache, if required.
     * 确保路径存在于缓存，如果有明确实现必要的话。
     * 对于GenericDistributedScopeTree::getPath, 默认会自动写入缓存，因此这里可以通过getPath保证路径缓存一定存在。
     * @param guid, target guid.
     * @return Path
     */
    protected void affirmPathExist( GUID guid ) {
        this.imperialTree.getCachePath( guid );
    }

    @Override
    public DeployTreeNode get( GUID guid ){
        return (DeployTreeNode) super.get( guid );
    }

    @Override
    public void update( TreeNode treeNode ) {
        TreeNodeOperator operator = this.operatorFactory.getOperator( treeNode.getMetaType() );
        operator.update( treeNode );
    }

    @Override
    public void remove( GUID guid ) {
        super.remove( guid );
    }

    @Override
    public void createDeployServiceInsMapping(GUID deployGuid, GUID serviceInsGuid) {
        GenericDeployInsMapping insMapping = new GenericDeployInsMapping();
        insMapping.setServiceInsGuid( serviceInsGuid );
        insMapping.setDeployGuid( deployGuid );
        this.deployServiceInsMappingManipulator.insert( insMapping );
    }
}
