package com.pinecone.hydra.service.kom;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;
import com.pinecone.hydra.service.kom.entity.ArchElementNode;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericNamespace;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.service.kom.entity.Namespace;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.entity.ServiceTreeNode;
import com.pinecone.hydra.service.kom.entity.ServoElement;
import com.pinecone.hydra.service.kom.operator.GenericElementOperatorFactory;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
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
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;

public class UniformServiceInstrument extends ArchReparseKOMTree implements ServiceInstrument {
    //GenericDistributedScopeTree
    protected ImperialTree                  imperialTree;

    protected ServiceMasterManipulator      serviceMasterManipulator;

    protected ServiceNamespaceManipulator   serviceNamespaceManipulator;

    protected ApplicationNodeManipulator    applicationNodeManipulator;

    protected ServiceNodeManipulator        serviceNodeManipulator;

    protected ServiceInstanceManipulator    serviceInstanceManipulator;

    protected List<GUIDNameManipulator >    folderManipulators;

    protected List<GUIDNameManipulator >    fileManipulators;



    public UniformServiceInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, ServiceInstrument parent, String name, @Nullable GuidAllocator guidAllocator ) {
        super( superiorProcess, masterManipulator, ServiceInstrument.KernelServiceConfig, parent, name, guidAllocator );

        this.serviceMasterManipulator    = (ServiceMasterManipulator) masterManipulator;
        this.serviceNamespaceManipulator = this.serviceMasterManipulator.getNamespaceManipulator();
        this.applicationNodeManipulator  = this.serviceMasterManipulator.getApplicationNodeManipulator();
        this.serviceNodeManipulator      = this.serviceMasterManipulator.getServiceNodeManipulator();
        this.serviceInstanceManipulator  = this.serviceMasterManipulator.getServiceInstanceManipulator();
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.serviceMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.imperialTree                = new RegimentedImperialTree(treeMasterManipulator);
        this.operatorFactory             = new GenericElementOperatorFactory(this,(ServiceMasterManipulator) masterManipulator);

        this.pathResolver                = new KOPathResolver( this.kernelObjectConfig );

        // TODO for customize service tree architecture.
        this.folderManipulators          = new ArrayList<>( List.of( this.serviceNamespaceManipulator, this.applicationNodeManipulator ) );
        this.fileManipulators            = new ArrayList<>( List.of( this.applicationNodeManipulator, this.serviceNodeManipulator ) );
        this.pathSelector                = new MultiFolderPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulators.toArray( new GUIDNameManipulator[]{} ), this.fileManipulators.toArray( new GUIDNameManipulator[]{} )
        );

        this.mReparseKOM                 =  new GenericReparseKOMTreeAddition( this );
    }

    public UniformServiceInstrument(Processum superiorProcess, KOIMasterManipulator masterManipulator ){
        this( superiorProcess, masterManipulator, null, ServiceInstrument.class.getSimpleName(), new GuidAllocator128V7());
    }

//    public UniformServiceInstrument( Hydrogen hydrogen ) {
//        this.hydrogen = hydrogen;
//    }

    public UniformServiceInstrument(KOIMappingDriver driver ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator()
        );
    }

    public UniformServiceInstrument(KOIMappingDriver driver, ServiceInstrument parent, String name ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                parent,
                name,
                null
        );
    }

    @Override
    public ServiceMasterManipulator getServiceMasterManipulator() {
        return this.serviceMasterManipulator;
    }

    protected ServiceTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        List<String > parts = this.pathResolver.resolvePathParts( path );
        this.assertValidTreePath( path, parts );
        String currentPath = "";
        GUID parentGuid = null;
        ElementNode parentElement = null;

        String normalizedPath = this.pathResolver.assemblePath( parts );
        ServiceTreeNode node = this.queryElement( normalizedPath );
        if ( node != null ){
            this.assertMatchedNodeType( normalizedPath, node, cnSup );
            return node;
        }

        ServiceTreeNode ret = null;
        for( int i = 0; i < parts.size(); ++i ){
            if ( i > 0 ) {
                currentPath = currentPath + this.getConfig().getPathNameSeparator();
            }
            currentPath = currentPath + parts.get( i );
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.size() - 1 && cnSup != null ){
                    this.assertCanAttach( parentElement, cnSup, currentPath );
                    ServoElement servoElement = (ServoElement) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    servoElement.setName( parts.get( i ) );
                    GUID guid = this.put( servoElement );
                    if ( parentGuid != null ) {
                        this.affirmOwnedNode( parentGuid, guid );
                    }
                    return servoElement;
                }
                else {
                    this.assertCanAttach( parentElement, nsSup, currentPath );
                    Namespace namespace = (Namespace) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    namespace.setName( parts.get( i ) );
                    GUID guid = this.put( namespace );
                    if ( parentGuid != null ){
                        this.affirmOwnedNode( parentGuid, guid );
                    }

                    parentGuid = guid;
                    parentElement = namespace.evinceElementNode();
                    ret = namespace;
                }
            }
            else {
                this.assertCanAttach( parentElement, node.evinceElementNode(), currentPath );
                if ( i == parts.size() - 1 ) {
                    this.assertMatchedNodeType( currentPath, node, cnSup );
                }
                parentGuid = node.getGuid();
                parentElement = node.evinceElementNode();
            }
        }

        return ret;
    }

    protected void assertValidTreePath( String path, List<String > parts ) {
        if ( parts == null || parts.isEmpty() ) {
            throw new IllegalArgumentException( "Service tree path should not be root or blank: " + path );
        }

        for ( String part : parts ) {
            if ( part == null || part.trim().isEmpty() ) {
                throw new IllegalArgumentException( "Service tree path should not contain blank segment: " + path );
            }
        }
    }

    protected void assertMatchedNodeType( String path, ServiceTreeNode node, Class<? > cnSup ) {
        ElementNode elementNode = node == null ? null : node.evinceElementNode();
        if ( elementNode == null ) {
            throw new IllegalArgumentException( "Service tree path does not point to an element node: " + path );
        }

        if ( cnSup == null ) {
            if ( elementNode.evinceNamespace() == null ) {
                throw new IllegalArgumentException( "Service tree path already exists but is not a namespace: " + path );
            }
        }
        else if ( cnSup == GenericApplicationElement.class ) {
            if ( elementNode.evinceApplicationElement() == null ) {
                throw new IllegalArgumentException( "Service tree path already exists but is not an application: " + path );
            }
        }
        else if ( cnSup == GenericServiceElement.class ) {
            if ( elementNode.evinceServiceElement() == null ) {
                throw new IllegalArgumentException( "Service tree path already exists but is not a service: " + path );
            }
        }
    }

    protected void assertCanAttach( ElementNode parentElement, Class<? > childClass, String path ) {
        if ( childClass == GenericServiceElement.class ) {
            this.assertCanAttachService( parentElement, path );
            return;
        }

        if ( parentElement != null && parentElement.evinceApplicationElement() != null ) {
            throw new IllegalArgumentException( "Application node can only contain service nodes: " + path );
        }

        if ( parentElement != null && parentElement.evinceServiceElement() != null ) {
            throw new IllegalArgumentException( "Service node cannot contain child nodes: " + path );
        }
    }

    protected void assertCanAttach( ElementNode parentElement, ElementNode childElement, String path ) {
        if ( childElement == null ) {
            throw new IllegalArgumentException( "Service tree child node is invalid: " + path );
        }

        if ( childElement.evinceServiceElement() != null ) {
            this.assertCanAttachService( parentElement, path );
            return;
        }

        if ( parentElement != null && parentElement.evinceApplicationElement() != null ) {
            throw new IllegalArgumentException( "Application node can only contain service nodes: " + path );
        }

        if ( parentElement != null && parentElement.evinceServiceElement() != null ) {
            throw new IllegalArgumentException( "Service node cannot contain child nodes: " + path );
        }
    }

    protected void assertCanAttachService( ElementNode parentElement, String path ) {
        if ( parentElement != null && parentElement.evinceServiceElement() != null ) {
            throw new IllegalArgumentException( "Service node cannot contain child nodes: " + path );
        }
    }

    @Override
    public ApplicationElement affirmApplication( String path ) {
        return (ApplicationElement) this.affirmTreeNodeByPath( path, GenericApplicationElement.class, GenericNamespace.class );
    }

    @Override
    public ServiceElement affirmService( String path ) {
        return (ServiceElement) this.affirmTreeNodeByPath( path, GenericServiceElement.class, GenericNamespace.class );
    }

    @Override
    public ElementNode queryElement( String path ) {
        List<String > parts = this.pathResolver.resolvePathParts( path );
        this.assertValidTreePath( path, parts );

        GUID guid = this.queryGUIDByPath( path );
        ElementNode node = this.queryElementByGuid( guid );
        if ( node != null ) {
            return node;
        }

        if ( guid != null ) {
            this.imperialTree.removeCachePath( guid );
        }

        return this.queryElementByPathParts( parts );
    }

    protected ElementNode queryElementByGuid( GUID guid ) {
        if ( guid == null ) {
            return null;
        }

        ServiceTreeNode node = this.get( guid );
        if ( node == null ) {
            return null;
        }

        return node.evinceElementNode();
    }

    protected ElementNode queryElementByPathParts( List<String > parts ) {
        GUID currentGuid = null;
        for ( int i = 0; i < parts.size(); ++i ) {
            currentGuid = this.queryChildGuid( currentGuid, parts.get( i ) );
            if ( currentGuid == null ) {
                return null;
            }
        }

        return this.queryElementByGuid( currentGuid );
    }

    protected GUID queryChildGuid( GUID parentGuid, String childName ) {
        for ( GUIDNameManipulator manipulator : this.folderManipulators ) {
            GUID guid = this.queryChildGuid( manipulator, parentGuid, childName );
            if ( guid != null ) {
                return guid;
            }
        }

        for ( GUIDNameManipulator manipulator : this.fileManipulators ) {
            GUID guid = this.queryChildGuid( manipulator, parentGuid, childName );
            if ( guid != null ) {
                return guid;
            }
        }

        return null;
    }

    protected GUID queryChildGuid( GUIDNameManipulator manipulator, GUID parentGuid, String childName ) {
        List<GUID > guids = manipulator.getGuidsByName( childName );
        for ( GUID guid : guids ) {
            if ( this.isMatchedChildGuid( parentGuid, guid ) ) {
                return guid;
            }
        }

        return null;
    }

    protected boolean isMatchedChildGuid( GUID parentGuid, GUID guid ) {
        if ( parentGuid == null ) {
            return this.imperialTree.isRoot( guid );
        }

        return this.imperialTree.fetchParentGuids( guid ).contains( parentGuid );
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
        ServiceTreeNode node = (ServiceTreeNode) super.get( guid );
        return this.applyServiceInstrument( node );
    }

    protected ServiceTreeNode applyServiceInstrument( ServiceTreeNode node ) {
        if ( node instanceof ArchElementNode ) {
            ( (ArchElementNode) node ).apply( this );
        }

        return node;
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
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        return null;
    }

    @Override
    public List<ServiceElement> fetchAllService() {
        return this.serviceNodeManipulator.fetchAllService();
    }

    @Override
    public List<ServiceElement> fetchServices( ServiceElementQuery query ) {
        return this.serviceNodeManipulator.fetchServices( query );
    }

    @Override
    public List<ServiceElement> fetchServicesByGuids( List<GUID> guids ) {
        return this.serviceNodeManipulator.fetchServicesByGuids( guids );
    }

    @Override
    public long countServices( ServiceElementQuery query ) {
        return this.serviceNodeManipulator.countServices( query );
    }

    @Override
    public ServiceElementPage fetchServicePage( ServiceElementQuery query ) {
        ServiceElementQuery safeQuery = query;
        if ( safeQuery == null ) {
            safeQuery = new ServiceElementQuery();
        }

        List<ServiceElement> items = this.fetchServices( safeQuery );
        long nTotal = this.countServices( safeQuery );
        return new ServiceElementPage( items, nTotal, safeQuery.getOffset(), safeQuery.getLimit() );
    }

    @Override
    public void createServiceInstance( ServiceInstanceEntry serviceInstanceEntry ) {
        this.serviceInstanceManipulator.initServiceInstance( serviceInstanceEntry );
    }

    @Override
    public ServiceInstanceEntry queryServiceInstance( GUID serviceId ) {
        return this.serviceInstanceManipulator.queryServiceInstance( serviceId );
    }

    @Override
    public List<ServiceInstanceEntry> fetchServiceInstances( ServiceInstanceQuery query ) {
        return this.serviceInstanceManipulator.fetchServiceInstances( query );
    }

    @Override
    public long countServiceInstances( ServiceInstanceQuery query ) {
        return this.serviceInstanceManipulator.countServiceInstances( query );
    }

    @Override
    public List<ServiceInstanceEntry> fetchServiceInstancesByServiceGuid( GUID serviceGuid ) {
        return this.serviceInstanceManipulator.fetchServiceInstancesByServiceGuid( serviceGuid );
    }

    @Override
    public List<ServiceInstanceEntry> fetchServiceInstancesByStatusAfterId( String status, long lastId, int limit ) {
        return this.serviceInstanceManipulator.fetchServiceInstancesByStatusAfterId( status, lastId, limit );
    }

    @Override
    public ServiceInstancePage fetchServiceInstancePage( ServiceInstanceQuery query ) {
        ServiceInstanceQuery safeQuery = query;
        if ( safeQuery == null ) {
            safeQuery = new ServiceInstanceQuery();
        }

        List<ServiceInstanceEntry> items = this.fetchServiceInstances( safeQuery );
        long nTotal = this.countServiceInstances( safeQuery );
        return new ServiceInstancePage( items, nTotal, safeQuery.getOffset(), safeQuery.getLimit() );
    }

    @Override
    public void updateServiceInstance( ServiceInstanceEntry element ) {
        this.serviceInstanceManipulator.updateServiceInstance( element );
    }

    @Override
    public int updateServiceInstanceStatusIfCurrentStatus(
            GUID instanceGuid,
            String expectedStatus,
            String status,
            LocalDateTime offlineTime,
            LocalDateTime latestEndTime
    ) {
        return this.serviceInstanceManipulator.updateServiceInstanceStatusIfCurrentStatus(
                instanceGuid,
                expectedStatus,
                status,
                offlineTime,
                latestEndTime
        );
    }
}
