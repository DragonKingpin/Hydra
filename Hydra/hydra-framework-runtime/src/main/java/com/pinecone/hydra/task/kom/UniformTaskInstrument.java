package com.pinecone.hydra.task.kom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.task.kom.digest.TaskTreeElementDigest;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.GenericAppElement;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.kom.instance.KernelInstanceInstrument;
import com.pinecone.hydra.task.kom.operator.GenericElementOperatorFactory;
import com.pinecone.hydra.task.kom.source.AppNodeManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskNamespaceManipulator;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.task.kom.source.TaskTreeDigestManipulator;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.GenericReparseKOMTreeAddition;
import com.pinecone.hydra.system.ko.kom.MultiFolderPathSelector;
import com.pinecone.hydra.task.kom.digest.TaskElementDigest;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.RegimentedImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;

public class UniformTaskInstrument extends ArchReparseKOMTree implements TaskInstrument {
    //GenericDistributedScopeTree
    protected ImperialTree                imperialTree;

    protected TaskMasterManipulator       taskMasterManipulator;

    protected TaskTreeDigestManipulator   taskTreeDigestManipulator;

    protected TaskNamespaceManipulator    taskNamespaceManipulator;

    protected AppNodeManipulator          appNodeManipulator;

    protected TaskNodeManipulator         taskNodeManipulator;

    protected List<GUIDNameManipulator >  folderManipulators;

    protected List<GUIDNameManipulator >  fileManipulators;

    protected InstanceInstrument          instanceInstrument;

    protected TaskScheduleSemanticValidator mTaskScheduleSemanticValidator;

    protected ThreadLocal<Boolean>          mTaskScheduleValidationSilenced;

    public UniformTaskInstrument(
            Processum superiorProcess, KOIMasterManipulator masterManipulator, TaskInstrument parent, String name, KernelObjectConfig config,
            @Nullable GuidAllocator guidAllocator
    ) {
        super( superiorProcess, masterManipulator, TaskInstrument.KernelServiceConfig, parent, name, guidAllocator );

        this.taskMasterManipulator       = (TaskMasterManipulator) masterManipulator;
        this.taskTreeDigestManipulator   = this.taskMasterManipulator.getTaskTreeDigestManipulator();
        this.taskNamespaceManipulator    = this.taskMasterManipulator.getNamespaceManipulator();
        this.appNodeManipulator          = this.taskMasterManipulator.getAppNodeManipulator();
        this.taskNodeManipulator         = this.taskMasterManipulator.getTaskNodeManipulator();
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.taskMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.imperialTree                = new RegimentedImperialTree(treeMasterManipulator);
        this.operatorFactory             = new GenericElementOperatorFactory(this,(TaskMasterManipulator) masterManipulator);
        this.pathResolver                = new KOPathResolver( this.kernelObjectConfig );

        // TODO for customize service tree architecture.
        this.folderManipulators          = new ArrayList<>( List.of( this.taskNamespaceManipulator, this.appNodeManipulator) );
        this.fileManipulators            = new ArrayList<>( List.of( this.appNodeManipulator, this.taskNodeManipulator) );
        this.pathSelector                = new MultiFolderPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulators.toArray( new GUIDNameManipulator[]{} ), this.fileManipulators.toArray( new GUIDNameManipulator[]{} )
        );
        this.mReparseKOM                 = new GenericReparseKOMTreeAddition( this );
        this.instanceInstrument          = new KernelInstanceInstrument( this, this.taskMasterManipulator.getInstanceNodeManipulator() );
        this.mTaskScheduleSemanticValidator = new TaskScheduleSemanticValidator();
        this.mTaskScheduleValidationSilenced = ThreadLocal.withInitial( () -> false );
        this.kernelObjectConfig          = config;
    }

    public UniformTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, KernelObjectConfig config ) {
        this( superiorProcess, masterManipulator, null, TaskInstrument.class.getSimpleName(), config, new GuidAllocator128V7());
    }

//    public UniformTaskInstrument( Hydrogen hydrogen ) {
//        this.hydrogen = hydrogen;
//    }

    public UniformTaskInstrument( KOIMappingDriver driver, KernelObjectConfig config ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                config
        );
    }

    public UniformTaskInstrument( KOIMappingDriver driver, TaskInstrument parent, String name, KernelObjectConfig config ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                parent,
                name,
                config,
                null
        );
    }

    protected TaskTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy128();

        TaskTreeNode node = this.queryElement(path);
        if ( node != null ){
            return node;
        }

        TaskTreeNode ret = null;
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
    public InstanceInstrument getInstanceInstrument() {
        return this.instanceInstrument;
    }

    @Override
    public TaskTreeElementDigest queryTaskTreeDigestByPath( String path ) {
        TaskTreeElementDigest digest = null;
        GUID guid = this.queryGUIDByPath( path );
        if ( guid != null ) {
            digest = this.taskTreeDigestManipulator.queryDigestByGuid( guid );
        }
        return this.rectifyTaskTreeDigestPath( digest, path, null );
    }

    @Override
    public TaskTreeElementDigest queryTaskTreeDigestByGuid( GUID guid ) {
        return this.rectifyTaskTreeDigestPath( this.taskTreeDigestManipulator.queryDigestByGuid( guid ), null, null );
    }

    @Override
    public List<TaskTreeElementDigest> fetchTaskTreeChildDigests( GUID parentGuid ) {
        return this.rectifyTaskTreeDigestPaths(
                this.taskTreeDigestManipulator.fetchChildDigests( parentGuid ),
                this.safeGetPath( parentGuid )
        );
    }

    @Override
    public List<TaskElementDigest> listTaskElementDigests( int offset, int pageSize ) {
        return this.rectifyTaskElementDigestPaths( this.taskNodeManipulator.listDigests( offset, pageSize ) );
    }

    @Override
    public List<TaskElementDigest> fetchTaskElementDigestsByGuids( Collection<GUID> guids ) {
        return this.rectifyTaskElementDigestPaths( this.taskNodeManipulator.fetchDigestsByGuids( guids ) );
    }

    protected List<TaskTreeElementDigest> rectifyTaskTreeDigestPaths( List<TaskTreeElementDigest> digests, String szParentPath ) {
        if ( digests == null || digests.isEmpty() ) {
            return digests;
        }

        for ( TaskTreeElementDigest digest : digests ) {
            this.rectifyTaskTreeDigestPath( digest, null, szParentPath );
        }
        return digests;
    }

    protected TaskTreeElementDigest rectifyTaskTreeDigestPath( TaskTreeElementDigest digest, String szRequestPath, String szParentPath ) {
        if ( digest == null ) {
            return null;
        }

        String szResolvedPath = this.resolveJoinedPath( digest.getPath(), digest.getLongPath() );
        if ( this.isBlank( szResolvedPath ) ) {
            szResolvedPath = this.safeGetPath( digest.getGuid() );
        }
        if ( this.isBlank( szResolvedPath ) ) {
            szResolvedPath = szRequestPath;
        }
        if ( this.isBlank( szResolvedPath ) ) {
            szResolvedPath = this.joinPath( szParentPath, this.resolveDigestName( digest ) );
        }
        if ( !this.isBlank( szResolvedPath ) ) {
            digest.setPath( szResolvedPath );
        }
        return digest;
    }

    protected List<TaskElementDigest> rectifyTaskElementDigestPaths( List<TaskElementDigest> digests ) {
        if ( digests == null || digests.isEmpty() ) {
            return digests;
        }

        for ( TaskElementDigest digest : digests ) {
            this.rectifyTaskElementDigestPath( digest );
        }
        return digests;
    }

    protected TaskElementDigest rectifyTaskElementDigestPath( TaskElementDigest digest ) {
        if ( digest == null ) {
            return null;
        }

        String szResolvedPath = this.resolveJoinedPath( digest.getKomPath(), digest.getSystemKernelObjectPath() );
        if ( this.isBlank( szResolvedPath ) ) {
            szResolvedPath = this.safeGetPath( digest.getGuid() );
        }
        if ( !this.isBlank( szResolvedPath ) ) {
            digest.setKomPath( szResolvedPath );
        }

        String szSystemPath = this.safeQuerySystemKernelObjectPath( digest.getGuid() );
        digest.setSystemKernelObjectPath( this.isBlank( szSystemPath ) ? szResolvedPath : szSystemPath );
        return digest;
    }

    protected String resolveJoinedPath( String szPath, String szLongPath ) {
        if ( this.isBlank( szPath ) ) {
            return szLongPath;
        }
        if ( this.isBlank( szLongPath ) ) {
            return szPath;
        }
        if ( szLongPath.startsWith( szPath ) ) {
            return szLongPath;
        }
        return szPath + szLongPath;
    }

    protected String safeGetPath( GUID guid ) {
        if ( guid == null ) {
            return null;
        }
        try {
            return this.getPath( guid );
        } catch ( RuntimeException exception ) {
            return null;
        }
    }

    protected String safeQuerySystemKernelObjectPath( GUID guid ) {
        if ( guid == null ) {
            return null;
        }
        try {
            return this.querySystemKernelObjectPath( guid );
        } catch ( RuntimeException exception ) {
            return null;
        }
    }

    protected String resolveDigestName( TaskTreeElementDigest digest ) {
        if ( digest == null ) {
            return null;
        }
        if ( !this.isBlank( digest.getName() ) ) {
            return digest.getName();
        }
        return digest.getGuid() == null ? null : digest.getGuid().toString();
    }

    protected String joinPath( String szParentPath, String szName ) {
        if ( this.isBlank( szName ) ) {
            return szParentPath;
        }
        if ( this.isBlank( szParentPath ) ) {
            return szName;
        }

        String szSeparator = this.kernelObjectConfig.getPathNameSeparator();
        String szNormalizedParent = szParentPath.endsWith( szSeparator )
                ? szParentPath.substring( 0, szParentPath.length() - szSeparator.length() )
                : szParentPath;
        return szNormalizedParent + szSeparator + szName;
    }

    protected boolean isBlank( String szValue ) {
        return szValue == null || szValue.trim().isEmpty();
    }

    @Override
    public AppElement affirmApp( String path ) {
        return ( AppElement ) this.affirmTreeNodeByPath( path, GenericAppElement.class, GenericNamespace.class );
    }

    @Override
    public TaskElement affirmTask( String path ,TaskElement metaInfos) {
        this.mTaskScheduleSemanticValidator.validate( metaInfos );

        boolean bOriginalSilenced = this.mTaskScheduleValidationSilenced.get();
        this.mTaskScheduleValidationSilenced.set( true );
        TaskElement taskElement;
        try {
            taskElement =  (TaskElement) this.affirmTreeNodeByPath( path, GenericTaskElement.class, GenericNamespace.class );
        }
        finally {
            this.mTaskScheduleValidationSilenced.set( bOriginalSilenced );
        }

        taskElement.setActuallyPriority( metaInfos.getActuallyPriority() );
        taskElement.setDeploymentMethod( metaInfos.getDeploymentMethod() );
        taskElement.setEnable( metaInfos.isEnable());
        taskElement.setDryRun( metaInfos.isDryRun() );
        taskElement.setTimeoutSeconds( metaInfos.getTimeoutSeconds() );
        taskElement.setRetryTimes( metaInfos.getRetryTimes() );
        taskElement.setRetryIntervalSeconds( metaInfos.getRetryIntervalSeconds() );
        taskElement.setPriority( metaInfos.getPriority() );
        taskElement.setResourceType( metaInfos.getResourceType() );
        taskElement.setScheduleCycle( metaInfos.getScheduleCycle() );
        taskElement.setScheduleType( metaInfos.getScheduleType() );
        taskElement.setType( metaInfos.getType() );
        taskElement.setImagePath( metaInfos.getImagePath() );
        taskElement.setExecArch( metaInfos.getExecArch() );
        taskElement.setName( metaInfos.getName() );
        taskElement.setGuid( metaInfos.getGuid() );
        this.mTaskScheduleSemanticValidator.validate( taskElement );
        return taskElement;
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
    public TaskTreeNode get( GUID guid ){
        return (TaskTreeNode) super.get( guid );
    }

    @Override
    public GUID put( TreeNode treeNode ) {
        if ( !this.mTaskScheduleValidationSilenced.get() ) {
            this.mTaskScheduleSemanticValidator.validateForPersistence( treeNode );
        }
        return super.put( treeNode );
    }

    @Override
    public void update( TreeNode treeNode ) {
        this.mTaskScheduleSemanticValidator.validateForPersistence( treeNode );
        TreeNodeOperator operator = this.operatorFactory.getOperator( treeNode.getMetaType() );
        operator.update( treeNode );
    }

    @Override
    public void remove( GUID guid ) {
        super.remove( guid );
    }

}
