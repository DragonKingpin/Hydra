package com.pinecone.hydra.task.kom;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.GenericJobElement;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.ServiceTreeNode;
import com.pinecone.hydra.task.kom.entity.ServoElement;
import com.pinecone.hydra.task.kom.operator.GenericElementOperatorFactory;
import com.pinecone.hydra.task.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.task.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskNamespaceManipulator;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.GenericReparseKOMTreeAddition;
import com.pinecone.hydra.system.ko.kom.MultiFolderPathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.RegimentedImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;

public class UniformTaskInstrument extends ArchReparseKOMTree implements ServiceInstrument {
    //GenericDistributedScopeTree
    protected ImperialTree                imperialTree;

    protected ServiceMasterManipulator    serviceMasterManipulator;

    protected TaskNamespaceManipulator taskNamespaceManipulator;

    protected ApplicationNodeManipulator  applicationNodeManipulator;

    protected TaskNodeManipulator taskNodeManipulator;

    protected List<GUIDNameManipulator >  folderManipulators;

    protected List<GUIDNameManipulator >  fileManipulators;



    public UniformTaskInstrument(Processum superiorProcess, KOIMasterManipulator masterManipulator, ServiceInstrument parent, String name ){
        super( superiorProcess, masterManipulator, ServiceInstrument.KernelServiceConfig, parent, name );
        Debug.trace(masterManipulator);
        this.hydrarum = hydrarum;
        this.serviceMasterManipulator    = (ServiceMasterManipulator) masterManipulator;
        this.taskNamespaceManipulator = serviceMasterManipulator.getNamespaceManipulator();
        this.applicationNodeManipulator  = serviceMasterManipulator.getApplicationNodeManipulator();
        this.taskNodeManipulator = serviceMasterManipulator.getServiceNodeManipulator();
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.serviceMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.imperialTree                = new RegimentedImperialTree(treeMasterManipulator);
        this.guidAllocator               = GUIDs.newGuidAllocator();
        this.operatorFactory             = new GenericElementOperatorFactory(this,(ServiceMasterManipulator) masterManipulator);

        this.pathResolver                = new KOPathResolver( this.kernelObjectConfig );

        // TODO for customize service tree architecture.
        this.folderManipulators          = new ArrayList<>( List.of( this.taskNamespaceManipulator, this.applicationNodeManipulator ) );
        this.fileManipulators            = new ArrayList<>( List.of( this.applicationNodeManipulator, this.taskNodeManipulator) );
        this.pathSelector                = new MultiFolderPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulators.toArray( new GUIDNameManipulator[]{} ), this.fileManipulators.toArray( new GUIDNameManipulator[]{} )
        );

        this.mReparseKOM                 =  new GenericReparseKOMTreeAddition( this );
    }

    public UniformTaskInstrument(Processum superiorProcess, KOIMasterManipulator masterManipulator ){
        this( superiorProcess, masterManipulator, null, ServiceInstrument.class.getSimpleName() );
    }

//    public UniformTaskInstrument( Hydrarum hydrarum ) {
//        this.hydrarum = hydrarum;
//    }

    public UniformTaskInstrument(KOIMappingDriver driver ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator()
        );
    }

    public UniformTaskInstrument(KOIMappingDriver driver, ServiceInstrument parent, String name ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                parent,
                name
        );
    }

    protected ServiceTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        ServiceTreeNode node = this.queryElement(path);
        if ( node != null ){
            return node;
        }

        ServiceTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    ServoElement servoElement = (ServoElement) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    servoElement.setName( parts[i] );
                    GUID guid = this.put( servoElement );
                    this.affirmOwnedNode( parentGuid, guid );
                    return servoElement;
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
    public JobElement affirmApplication(String path ) {
        return (JobElement) this.affirmTreeNodeByPath( path, GenericJobElement.class, GenericNamespace.class );
    }

    @Override
    public TaskElement affirmService(String path ) {
        return (TaskElement) this.affirmTreeNodeByPath( path, GenericTaskElement.class, GenericNamespace.class );
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
    public ServiceTreeNode get( GUID guid ){
        return (ServiceTreeNode) super.get( guid );
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
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public List<TaskElement> fetchAllService() {
        return this.taskNodeManipulator.fetchAllService();
    }
}
