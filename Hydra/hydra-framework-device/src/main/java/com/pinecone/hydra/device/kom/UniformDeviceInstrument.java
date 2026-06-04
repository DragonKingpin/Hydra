package com.pinecone.hydra.device.kom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.generic.GenericDeviceSchema;
import com.pinecone.hydra.device.generic.GenericDeviceSchemaDesigner;
import com.pinecone.hydra.device.generic.GenericDeviceSchemaTransformer;
import com.pinecone.hydra.device.generic.GenericDeviceSchemaValidator;
import com.pinecone.hydra.device.generic.GenericDeviceType;
import com.pinecone.hydra.device.kom.entity.ClusterElement;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.device.kom.entity.GenericContainerElement;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.device.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.GenericQuickElement;
import com.pinecone.hydra.device.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.QuickElement;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.device.kom.source.QuickElementManipulator;
import com.pinecone.hydra.device.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.GenericReparseKOMTreeAddition;
import com.pinecone.hydra.system.ko.kom.MultiFolderPathSelector;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.kom.entity.GenericClusterElement;
import com.pinecone.hydra.device.kom.entity.GenericNamespace;
import com.pinecone.hydra.device.kom.entity.Namespace;
import com.pinecone.hydra.device.kom.entity.DeviceTreeNode;
import com.pinecone.hydra.device.kom.operator.GenericElementOperatorFactory;
import com.pinecone.hydra.device.kom.source.ClusterNodeManipulator;
import com.pinecone.hydra.device.kom.source.ContainerElementManipulator;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.device.kom.source.DeviceNamespaceManipulator;
import com.pinecone.hydra.device.kom.source.DeviceInstanceManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceSchemaManipulator;
import com.pinecone.hydra.device.kom.source.GenericDeviceTypeManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.RegimentedImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;

public class UniformDeviceInstrument extends ArchReparseKOMTree implements DeviceInstrument {
    //GenericDistributedScopeTree
    protected ImperialTree                          imperialTree;

    protected DeviceMasterManipulator               deviceMasterManipulator;

    protected DeviceNamespaceManipulator            deviceNamespaceManipulator;

    protected ClusterNodeManipulator                clusterNodeManipulator;

    protected List<GUIDNameManipulator >            folderManipulators;

    protected List<GUIDNameManipulator >            fileManipulators;

    protected PhysicalHostManipulator               physicalHostManipulator;

    protected VirtualMachineManipulator             virtualMachineManipulator;

    protected QuickElementManipulator               quickElementManipulator;

    protected ContainerElementManipulator           containerElementManipulator;

    protected GenericDeviceManipulator              genericDeviceManipulator;

    protected GenericDeviceTypeManipulator          genericDeviceTypeManipulator;

    protected GenericDeviceSchemaManipulator        genericDeviceSchemaManipulator;

    protected DeviceInstanceManipulator             deviceInstanceManipulator;

    protected GenericDeviceSchemaValidator          genericDeviceSchemaValidator;

    protected GenericDeviceSchemaTransformer        genericDeviceSchemaTransformer;

    public UniformDeviceInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, DeviceInstrument parent, String name, @Nullable GuidAllocator guidAllocator ) {
        super( superiorProcess, masterManipulator, DeviceInstrument.KERNEL_DEVICE_CONFIG, parent, name, guidAllocator );

        this.deviceMasterManipulator = (DeviceMasterManipulator) masterManipulator;
        this.deviceNamespaceManipulator = this.deviceMasterManipulator.getNamespaceManipulator();
        this.clusterNodeManipulator          = this.deviceMasterManipulator.getJobNodeManipulator();
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.deviceMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.imperialTree                = new RegimentedImperialTree(treeMasterManipulator);
        this.operatorFactory             = new GenericElementOperatorFactory(this,(DeviceMasterManipulator) masterManipulator);
        this.physicalHostManipulator     = this.deviceMasterManipulator.getPhysicalHostManipulator();
        this.virtualMachineManipulator   = this.deviceMasterManipulator.getVirtualMachineManipulator();
        this.containerElementManipulator = this.deviceMasterManipulator.getContainerElementManipulator();
        this.genericDeviceManipulator    = this.deviceMasterManipulator.getGenericDeviceManipulator();
        this.genericDeviceTypeManipulator = this.deviceMasterManipulator.getGenericDeviceTypeManipulator();
        this.genericDeviceSchemaManipulator = this.deviceMasterManipulator.getGenericDeviceSchemaManipulator();
        this.deviceInstanceManipulator = this.deviceMasterManipulator.getDeviceInstanceManipulator();
        this.genericDeviceSchemaValidator = new GenericDeviceSchemaValidator();
        this.genericDeviceSchemaTransformer = new GenericDeviceSchemaTransformer();
        this.pathResolver                = new KOPathResolver( this.kernelObjectConfig );
        this.quickElementManipulator     = this.deviceMasterManipulator.getQuickElementManipulator();
        // TODO for customize service tree architecture.
        this.folderManipulators          = new ArrayList<>( List.of(
                this.deviceNamespaceManipulator,
                this.clusterNodeManipulator,
                this.physicalHostManipulator,
                this.virtualMachineManipulator,
                this.containerElementManipulator,
                this.genericDeviceManipulator
        ) );
        this.fileManipulators            = new ArrayList<>( List.of(
                this.clusterNodeManipulator,
                this.physicalHostManipulator,
                this.virtualMachineManipulator,
                this.containerElementManipulator,
                this.quickElementManipulator,
                this.genericDeviceManipulator
        ) );
        this.pathSelector                = new MultiFolderPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulators.toArray( new GUIDNameManipulator[]{} ), this.fileManipulators.toArray( new GUIDNameManipulator[]{} )
        );

        this.mReparseKOM                 =  new GenericReparseKOMTreeAddition( this );
    }

    public UniformDeviceInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, DeviceInstrument.class.getSimpleName(), new GuidAllocator128V7() );
    }

//    public UniformTaskInstrument( Hydrogen hydrogen ) {
//        this.hydrogen = hydrogen;
//    }

    public UniformDeviceInstrument( KOIMappingDriver driver ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator()
        );
    }

    public UniformDeviceInstrument( KOIMappingDriver driver, DeviceInstrument parent, String name ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                parent,
                name,
                null
        );
    }

    protected DeviceTreeNode affirmTreeNodeByPath(String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy128();

        DeviceTreeNode node = this.queryElement(path);
        if ( node != null ){
            return node;
        }

        DeviceTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = i == 0 ? this.queryElement( currentPath ) : this.queryDirectChild( parentGuid, parts[ i ] );
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

    protected DeviceTreeNode queryDirectChild( GUID parentGuid, String childName ) {
        if ( parentGuid == null ) {
            return null;
        }

        List<GUID > childGuids = this.fetchChildrenGuids( parentGuid );
        for( GUID childGuid : childGuids ) {
            DeviceTreeNode child = this.get( childGuid );
            if ( child != null && childName.equals( child.getName() ) ) {
                return child;
            }
        }

        return null;
    }

    @Override
    public ClusterElement affirmCluster(String path ) {
        return (ClusterElement) this.affirmTreeNodeByPath( path, GenericClusterElement.class, GenericNamespace.class );
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
    public GenericDeviceElement affirmGenericDevice( String path, String genericDevTypeCode, String schemaDataJson ) {
        GenericDeviceType type = this.queryGenericDeviceType( genericDevTypeCode );
        if ( type == null ) {
            throw new IllegalArgumentException( "Generic device type does not exist: " + genericDevTypeCode );
        }
        if ( !type.isEnabled() ) {
            throw new IllegalArgumentException( "Generic device type is disabled: " + genericDevTypeCode );
        }

        GenericDeviceSchema schema = type.getSchemaGuid() == null ? null : this.queryGenericDeviceSchema( type.getSchemaGuid() );
        this.genericDeviceSchemaValidator.validate( schema, schemaDataJson );

        DeviceTreeNode node = this.affirmGenericDeviceTreeNodeByPath( path, type, schemaDataJson );
        if ( !( node instanceof GenericDeviceElement ) ) {
            throw new IllegalArgumentException( "Device path has been occupied by non-generic element: " + path );
        }

        GenericDeviceElement element = (GenericDeviceElement) node;
        element.setGenericDevTypeCode( type.getCode() );
        element.setSchemaGuid( type.getSchemaGuid() == null ? null : type.getSchemaGuid().toString() );
        element.setSchemaDataJson( schemaDataJson );
        this.update( element );
        return element;
    }

    @Override
    public GenericDeviceSchema affirmGenericDeviceSchema( GenericDeviceSchema schema ) {
        if ( schema == null ) {
            throw new IllegalArgumentException( "Generic device schema is required." );
        }
        this.validateGenericDeviceSchema( schema );
        if ( schema.getGuid() == null ) {
            schema.setGuid( this.getGuidAllocator().nextGUID() );
        }
        if ( schema.getVersion() == null || schema.getVersion().trim().isEmpty() ) {
            schema.setVersion( "1.0.0" );
        }

        GenericDeviceSchema existedSchema = this.queryGenericDeviceSchemaByCode( schema.getCode() );
        if ( existedSchema == null ) {
            this.fillGenericDeviceSchemaCreateTime( schema );
            this.genericDeviceSchemaManipulator.insert( schema );
            return schema;
        }

        schema.setGuid( existedSchema.getGuid() );
        this.fillGenericDeviceSchemaUpdateTime( schema, existedSchema );
        this.genericDeviceSchemaManipulator.update( schema );
        return schema;
    }

    @Override
    public GenericDeviceSchema affirmGenericDeviceSchema( GenericDeviceSchema schema, GenericDeviceSchemaDesigner designer ) {
        if ( schema == null ) {
            throw new IllegalArgumentException( "Generic device schema is required." );
        }
        schema.setSchemaJson( this.genericDeviceSchemaTransformer.encode( designer ) );
        return this.affirmGenericDeviceSchema( schema );
    }

    @Override
    public GenericDeviceType affirmGenericDeviceType( GenericDeviceType type ) {
        if ( type == null ) {
            throw new IllegalArgumentException( "Generic device type is required." );
        }
        this.validateGenericDeviceType( type );
        if ( type.getGuid() == null ) {
            type.setGuid( this.getGuidAllocator().nextGUID() );
        }

        GenericDeviceType existedType = this.queryGenericDeviceType( type.getCode() );
        if ( existedType == null ) {
            this.fillGenericDeviceTypeCreateTime( type );
            this.genericDeviceTypeManipulator.insert( type );
            return type;
        }

        type.setGuid( existedType.getGuid() );
        this.fillGenericDeviceTypeUpdateTime( type, existedType );
        this.genericDeviceTypeManipulator.update( type );
        return type;
    }

    protected DeviceTreeNode affirmGenericDeviceTreeNodeByPath( String path, GenericDeviceType type, String schemaDataJson ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy128();

        DeviceTreeNode node = this.queryElement( path );
        if ( node != null ) {
            return node;
        }

        DeviceTreeNode ret = null;
        for ( int i = 0; i < parts.length; ++i ) {
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = i == 0 ? this.queryElement( currentPath ) : this.queryDirectChild( parentGuid, parts[ i ] );
            if ( node == null ) {
                if ( i == parts.length - 1 ) {
                    GenericDeviceElement element = new GenericDeviceElement( this );
                    element.setName( parts[ i ] );
                    element.setGenericDevTypeCode( type.getCode() );
                    element.setSchemaGuid( type.getSchemaGuid() == null ? null : type.getSchemaGuid().toString() );
                    element.setSchemaDataJson( schemaDataJson );
                    GUID guid = this.put( element );
                    this.affirmOwnedNode( parentGuid, guid );
                    return element;
                }

                Namespace namespace = new GenericNamespace( this );
                namespace.setName( parts[ i ] );
                GUID guid = this.put( namespace );
                if ( i != 0 ) {
                    this.affirmOwnedNode( parentGuid, guid );
                }
                parentGuid = guid;
                ret = namespace;
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }

    @Override
    public GenericDeviceElement queryGenericDevice( GUID guid ) {
        DeviceTreeNode node = this.get( guid );
        return node instanceof GenericDeviceElement ? (GenericDeviceElement) node : null;
    }

    @Override
    public Collection<GenericDeviceSchema> fetchGenericDeviceSchemas() {
        return this.genericDeviceSchemaManipulator.fetchGenericDeviceSchemas();
    }

    @Override
    public Collection<GenericDeviceType> fetchGenericDeviceTypes() {
        return this.genericDeviceTypeManipulator.fetchGenericDeviceTypes();
    }

    @Override
    public GenericDeviceType queryGenericDeviceType( String code ) {
        if ( code == null || code.trim().isEmpty() ) {
            return null;
        }
        return this.genericDeviceTypeManipulator.getGenericDeviceTypeByCode( code );
    }

    @Override
    public GenericDeviceSchema queryGenericDeviceSchema( GUID guid ) {
        if ( guid == null ) {
            return null;
        }
        return this.genericDeviceSchemaManipulator.getGenericDeviceSchema( guid );
    }

    @Override
    public GenericDeviceSchema queryGenericDeviceSchemaByCode( String code ) {
        if ( code == null || code.trim().isEmpty() ) {
            return null;
        }
        return this.genericDeviceSchemaManipulator.getGenericDeviceSchemaByCode( code );
    }

    protected void validateGenericDeviceSchema( GenericDeviceSchema schema ) {
        if ( schema.getCode() == null || schema.getCode().trim().isEmpty() ) {
            throw new IllegalArgumentException( "Generic device schema code is required." );
        }
        if ( schema.getName() == null || schema.getName().trim().isEmpty() ) {
            throw new IllegalArgumentException( "Generic device schema name is required." );
        }
        this.genericDeviceSchemaTransformer.decode( schema.getSchemaJson() );
    }

    protected void validateGenericDeviceType( GenericDeviceType type ) {
        if ( type.getCode() == null || type.getCode().trim().isEmpty() ) {
            throw new IllegalArgumentException( "Generic device type code is required." );
        }
        if ( type.getName() == null || type.getName().trim().isEmpty() ) {
            throw new IllegalArgumentException( "Generic device type name is required." );
        }
        if ( type.getSchemaGuid() != null && this.queryGenericDeviceSchema( type.getSchemaGuid() ) == null ) {
            throw new IllegalArgumentException( "Generic device schema does not exist: " + type.getSchemaGuid() );
        }
    }

    protected void fillGenericDeviceSchemaCreateTime( GenericDeviceSchema schema ) {
        LocalDateTime now = LocalDateTime.now();
        if ( schema.getCreateTime() == null ) {
            schema.setCreateTime( now );
        }
        schema.setUpdateTime( now );
    }

    protected void fillGenericDeviceSchemaUpdateTime( GenericDeviceSchema schema, GenericDeviceSchema existedSchema ) {
        if ( schema.getCreateTime() == null ) {
            schema.setCreateTime( existedSchema.getCreateTime() );
        }
        schema.setUpdateTime( LocalDateTime.now() );
    }

    protected void fillGenericDeviceTypeCreateTime( GenericDeviceType type ) {
        LocalDateTime now = LocalDateTime.now();
        if ( type.getCreateTime() == null ) {
            type.setCreateTime( now );
        }
        type.setUpdateTime( now );
    }

    protected void fillGenericDeviceTypeUpdateTime( GenericDeviceType type, GenericDeviceType existedType ) {
        if ( type.getCreateTime() == null ) {
            type.setCreateTime( existedType.getCreateTime() );
        }
        type.setUpdateTime( LocalDateTime.now() );
    }

    @Override
    public ElementNode queryElement( String path ) {
        DeviceTreeNode node = this.queryElementByDirectPath( path );
        if( node instanceof ElementNode ) {
            return (ElementNode) node;
        }

        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return this.get( guid ).evinceElementNode();
        }

        return null;
    }

    @Override
    public void createDeviceInstance( DeviceInstanceEntry deviceInstanceEntry ) {
        this.deviceInstanceManipulator.initDeviceInstance( deviceInstanceEntry );
    }

    @Override
    public DeviceInstanceEntry queryDeviceInstance( GUID instanceGuid ) {
        return this.deviceInstanceManipulator.queryDeviceInstance( instanceGuid );
    }

    @Override
    public Collection<DeviceInstanceEntry> fetchDeviceInstances( DeviceInstanceQuery query ) {
        return this.deviceInstanceManipulator.fetchDeviceInstances( query );
    }

    @Override
    public long countDeviceInstances( DeviceInstanceQuery query ) {
        return this.deviceInstanceManipulator.countDeviceInstances( query );
    }

    @Override
    public Collection<DeviceInstanceEntry> fetchDeviceInstancesByDeviceGuid( GUID deviceGuid ) {
        return this.deviceInstanceManipulator.fetchDeviceInstancesByDeviceGuid( deviceGuid );
    }

    @Override
    public DeviceInstancePage fetchDeviceInstancePage( DeviceInstanceQuery query ) {
        DeviceInstanceQuery safeQuery = query == null ? new DeviceInstanceQuery() : query;
        return new DeviceInstancePage(
                new ArrayList<>( this.fetchDeviceInstances( safeQuery ) ),
                this.countDeviceInstances( safeQuery ),
                safeQuery.getOffset(),
                safeQuery.getLimit()
        );
    }

    @Override
    public void updateDeviceInstance( DeviceInstanceEntry deviceInstanceEntry ) {
        this.deviceInstanceManipulator.updateDeviceInstance( deviceInstanceEntry );
    }

    protected DeviceTreeNode queryElementByDirectPath( String path ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        if ( parts.length == 0 ) {
            return null;
        }

        DeviceTreeNode node = this.queryRootByName( parts[ 0 ] );
        if ( node == null ) {
            return null;
        }

        for( int i = 1; i < parts.length; ++i ) {
            node = this.queryDirectChild( node.getGuid(), parts[ i ] );
            if ( node == null ) {
                return null;
            }
        }

        return node;
    }

    protected DeviceTreeNode queryRootByName( String name ) {
        Set<GUID > candidates = new HashSet<>();
        this.collectGuidsByName( this.folderManipulators, name, candidates );
        this.collectGuidsByName( this.fileManipulators, name, candidates );

        for( GUID guid : candidates ) {
            if ( this.imperialTree.isRoot( guid ) ) {
                DeviceTreeNode node = this.get( guid );
                if ( node != null && name.equals( node.getName() ) ) {
                    return node;
                }
            }
        }

        return null;
    }

    protected void collectGuidsByName( List<GUIDNameManipulator > manipulators, String name, Set<GUID > candidates ) {
        for( GUIDNameManipulator manipulator : manipulators ) {
            List<GUID > guids = manipulator.getGuidsByName( name );
            if( guids != null ) {
                candidates.addAll( guids );
            }
        }
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
    public DeviceTreeNode get( GUID guid ){
        return (DeviceTreeNode) super.get( guid );
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

}
