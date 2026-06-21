package com.walnut.odin.task;

import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.digest.TaskElementDigest;
import com.pinecone.hydra.task.kom.digest.TaskTreeElementDigest;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;


import com.walnut.odin.task.service.CategoryService;
import com.walnut.odin.task.service.RavenCategoryService;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;

import com.walnut.odin.project.RavenTaskProjectInstrument;
import com.walnut.odin.project.TaskProjectInstrument;
import com.walnut.odin.specific.RavenTaskSpecificService;
import com.walnut.odin.specific.TaskSpecificService;
import com.walnut.odin.task.system.TaskPathInvalidException;
import com.walnut.odin.task.troll.GenericRavenTask;

public class RavenTaskInstrument implements CentralizedTaskInstrument {
    protected RavenTaskMasterManipulator ravenTaskMasterManipulator;

    protected UniformTaskInstrument      uniformTaskInstrument;

    protected CategoryService            categoryService;

    protected TaskProjectInstrument      taskProjectInstrument;

    protected TaskSpecificService        taskSpecificService;


    protected void overrideTaskInstrument( Processum superiorProcess, TaskMappingDriver driver, TaskInstrument parent, String name, KernelObjectConfig config, @Nullable GuidAllocator guidAllocator ) {
        this.uniformTaskInstrument      = new UniformTaskInstrument( superiorProcess, driver.getMasterManipulator(), parent, name, config, guidAllocator ) {
        /*    @Override
            public RavenTaskElement affirmTask( String path ,TaskElement metaInfos ) {
                TaskElement taskElement           = super.affirmTask( path , metaInfos);
                if ( taskElement == null ) {
                    return null;
                }

                return RavenTaskInstrument.this.transformTaskElement( taskElement, true );
            }

            @Override
            public ElementNode queryElement( String path ) {
                ElementNode proto = super.queryElement( path );
                if ( proto instanceof TaskElement ) {
                    return RavenTaskInstrument.this.transformTaskElement( (TaskElement) proto, false );
                }

                return proto;
            }

            @Override
            public TaskTreeNode get( GUID guid ) {
                TaskTreeNode treeNode = super.get( guid );
                return RavenTaskInstrument.this.transformTreeNode( treeNode, false );
            }

            @Override
            public TreeNode get( GUID guid, int depth ) {
                TreeNode treeNode = super.get( guid, depth );
                return RavenTaskInstrument.this.transformTreeNode( (TaskTreeNode) treeNode, false );
            }

            @Override
            public TreeNode getAsRootDepth( GUID guid ) {
                TreeNode treeNode =  super.getAsRootDepth( guid );
                return RavenTaskInstrument.this.transformTreeNode( (TaskTreeNode) treeNode, false );
            }*/
        };
    }

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, TaskInstrument parent, String name, KernelObjectConfig config, @Nullable GuidAllocator guidAllocator ) {
        this.ravenTaskMasterManipulator = (RavenTaskMasterManipulator) masterManipulator;
        TaskMappingDriver driver        = (TaskMappingDriver) this.ravenTaskMasterManipulator.getTaskMappingDriver();
        this.overrideTaskInstrument     ( superiorProcess, driver, parent, name, config, guidAllocator );

        this.categoryService            = new RavenCategoryService( this );
        this.taskProjectInstrument      = new RavenTaskProjectInstrument( this.ravenTaskMasterManipulator.getTaskProjectManipulator() );
        this.taskSpecificService        = new RavenTaskSpecificService( this.ravenTaskMasterManipulator.getTaskSpecificManipulator() );

    }

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, KernelObjectConfig config ) {
        this( superiorProcess, masterManipulator, null, CentralizedTaskInstrument.class.getSimpleName(), config, null );
    }

    public RavenTaskInstrument( KOIMappingDriver driver, CentralizedTaskInstrument parent, String name, KernelObjectConfig config ){
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name, config, null );
    }

    public RavenTaskInstrument( KOIMappingDriver driver, KernelObjectConfig config ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), config );
    }

    @Override
    public void applyGuidAllocator( GuidAllocator guidAllocator ) {
        this.uniformTaskInstrument.applyGuidAllocator( guidAllocator );
    }

    @Override
    public InstanceInstrument getInstanceInstrument() {
        return this.uniformTaskInstrument.getInstanceInstrument();
    }

    @Override
    public TaskTreeElementDigest queryTaskTreeDigestByPath( String path ) {
        return this.uniformTaskInstrument.queryTaskTreeDigestByPath( path );
    }

    @Override
    public TaskTreeElementDigest queryTaskTreeDigestByGuid( GUID guid ) {
        return this.uniformTaskInstrument.queryTaskTreeDigestByGuid( guid );
    }

    @Override
    public List<TaskTreeElementDigest> fetchTaskTreeChildDigests( GUID parentGuid ) {
        return this.uniformTaskInstrument.fetchTaskTreeChildDigests( parentGuid );
    }

    @Override
    public List<TaskElementDigest> listTaskElementDigests( int offset, int pageSize ) {
        return this.uniformTaskInstrument.listTaskElementDigests( offset, pageSize );
    }

    @Override
    public List<TaskElementDigest> fetchTaskElementDigestsByGuids( Collection<GUID> guids ) {
        return this.uniformTaskInstrument.fetchTaskElementDigestsByGuids( guids );
    }

    @Override
    public GUID assertGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException {
        GUID guid = this.uniformTaskInstrument.queryGUIDByPath( taskTreePath );
        if ( guid == null ) {
            throw new TaskPathInvalidException( taskTreePath );
        }

        return guid;
    }

    @Override
    public GUID assertTaskGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException, IllegalArgumentException {
        ElementNode node = this.uniformTaskInstrument.queryElement( taskTreePath );
        if ( node == null ) {
            throw new TaskPathInvalidException( taskTreePath );
        }
        if ( node.evinceTaskElement() == null ) {
            throw new IllegalArgumentException( "Path `" + taskTreePath + "` is not a task." );
        }

        return node.getGuid();
    }


    @Override
    public UniformTaskInstrument getUniformTaskInstrument() {
        return this.uniformTaskInstrument;
    }

    @Override
    public RavenTaskMasterManipulator getRavenTaskMasterManipulator() {
        return this.ravenTaskMasterManipulator;
    }

    @Override
    public TaskProjectInstrument getTaskProjectInstrument() {
        return this.taskProjectInstrument;
    }

    @Override
    public TaskSpecificService getTaskSpecificService() {
        return this.taskSpecificService;
    }

    @Override
    public void newLinkTag( String originalPath, String dirPath, String tagName ) {
        this.uniformTaskInstrument.newLinkTag( originalPath, dirPath, tagName );
    }

    @Override
    public void removeReparseLink( GUID guid ) {
        this.uniformTaskInstrument.removeReparseLink( guid );
    }

    @Override
    public void affirmOwnedNode( GUID parentGuid, GUID childGuid ) {
        this.uniformTaskInstrument.affirmOwnedNode( parentGuid, childGuid );
    }

    @Override
    public void newHardLink( GUID sourceGuid, GUID targetGuid ) {
        this.uniformTaskInstrument.newHardLink( sourceGuid, targetGuid );
    }

    @Override
    public void newLinkTag( GUID originalGuid, GUID dirGuid, String tagName ) {
        this.uniformTaskInstrument.newLinkTag( originalGuid, dirGuid, tagName );
    }

    @Override
    public void updateLinkTag( GUID tagGuid, String tagName ) {
        this.uniformTaskInstrument.updateLinkTag( tagGuid, tagName );
    }

    @Override
    public ReparseLinkNode queryReparseLinkByNS( String path, String szBadSep, String szTargetSep ) {
        return this.uniformTaskInstrument.queryReparseLinkByNS( path, szBadSep, szTargetSep );
    }

    @Override
    public ReparseLinkNode queryReparseLink( String path ) {
        return this.uniformTaskInstrument.queryReparseLink( path );
    }

    @Override
    public CategoryService getCategoryService() {
        return this.categoryService;
    }




    @Override
    public AppElement affirmApp( String path ) {
        return this.uniformTaskInstrument.affirmApp( path );
    }

    @Override
    public Namespace affirmNamespace( String path ) {
        return this.uniformTaskInstrument.affirmNamespace( path );
    }

    @Override
    public TaskElement affirmTask( String path ,TaskElement metaInfos) {
        return (TaskElement) this.uniformTaskInstrument.affirmTask( path ,metaInfos);
    }

    @Override
    public ElementNode queryElement( String path ) {
        return this.uniformTaskInstrument.queryElement( path );
    }

    @Override
    public boolean containsChild( GUID parentGuid, String childName ) {
        return this.uniformTaskInstrument.containsChild( parentGuid, childName );
    }

    @Override
    public void update( TreeNode treeNode ) {
        this.uniformTaskInstrument.update( treeNode );
    }

    @Override
    public TreeNode get( GUID guid ) {
        return this.uniformTaskInstrument.get( guid );
    }

    @Override
    public TreeNode get( GUID guid, int depth ) {
        return this.uniformTaskInstrument.get( guid, depth );
    }

    @Override
    public TreeNode getAsRootDepth( GUID guid ) {
        return this.uniformTaskInstrument.getAsRootDepth( guid );
    }




    /** Directly proxied **/

    @Override
    public KOMInstrument parent() {
        return this.uniformTaskInstrument.parent();
    }

    @Override
    public void setParent( CascadeInstrument parent ) {
        this.uniformTaskInstrument.setParent( parent );
    }

    @Override
    public com.pinecone.framework.util.name.Namespace getTargetingName() {
        return this.uniformTaskInstrument.getTargetingName();
    }

    @Override
    public void setTargetingName( com.pinecone.framework.util.name.Namespace name ) {
        this.uniformTaskInstrument.setTargetingName( name );
    }

    @Override
    public String getPath( GUID guid ) {
        return this.uniformTaskInstrument.getPath( guid );
    }

    @Override
    public String querySystemKernelObjectPath( GUID objectGuid ) {
        return this.uniformTaskInstrument.querySystemKernelObjectPath( objectGuid );
    }

    @Override
    public String getFullName( GUID guid ) {
        return this.uniformTaskInstrument.getFullName( guid );
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.uniformTaskInstrument.queryGUIDByPath( path );
    }

    @Override
    public GUID queryGUIDByFN( String fullName ) {
        return this.uniformTaskInstrument.queryGUIDByFN( fullName );
    }

    @Override
    public boolean contains( GUID nodeGuid ) {
        return this.uniformTaskInstrument.contains( nodeGuid );
    }

    @Override
    public GUID put( TreeNode treeNode ) {
        return this.uniformTaskInstrument.put( treeNode );
    }

    @Override
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        return this.uniformTaskInstrument.queryGUIDByNS( path, szBadSep, szTargetSep );
    }

    @Override
    public void remove( GUID guid ) {
        this.uniformTaskInstrument.remove( guid );
    }

    @Override
    public void remove( String path ) {
        this.uniformTaskInstrument.remove( path );
    }

    @Override
    public List<TreeNode> getChildren( GUID guid ) {
        return this.uniformTaskInstrument.getChildren( guid );
    }

    @Override
    public List<GUID> fetchChildrenGuids( GUID guid ) {
        return this.uniformTaskInstrument.fetchChildrenGuids( guid );
    }

    @Override
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        return this.uniformTaskInstrument.queryEntityHandleByNS( path, szBadSep, szTargetSep );
    }

    @Override
    public EntityNode queryNode( String path ) {
        return this.uniformTaskInstrument.queryNode( path );
    }

    @Override
    public TreeNode queryTreeNode( String path ) {
        return this.uniformTaskInstrument.queryTreeNode( path );
    }

    @Override
    public List<? extends TreeNode> fetchRoot() {
        return this.uniformTaskInstrument.fetchRoot();
    }

    @Override
    public void rename( GUID guid, String name ) {
        this.uniformTaskInstrument.rename( guid, name );
    }

    @Override
    public Processum getSuperiorProcess() {
        return this.uniformTaskInstrument.getSuperiorProcess();
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.uniformTaskInstrument.getGuidAllocator();
    }

    @Override
    public ImperialTree getMasterTrieTree() {
        return this.uniformTaskInstrument.getMasterTrieTree();
    }

    @Override
    public KernelObjectConfig getConfig() {
        return this.uniformTaskInstrument.getConfig();
    }

    @Override
    public String getSuperiorPathScope() {
        return this.uniformTaskInstrument.getSuperiorPathScope();
    }

    @Override
    public void applySuperiorPathScope( String superiorPathScope ) {
        this.uniformTaskInstrument.applySuperiorPathScope( superiorPathScope );
    }






    @Override
    public RavenTask constructTask( TaskElement taskElement ) {
        return this.constructTask( taskElement, null );
    }

    @Override
    public RavenTask constructTask( TaskElement taskElement, @Nullable Identification serviceId ) {
        if ( serviceId == null ) {
            //serviceId = taskElement.
        }
        RavenTask task = new GenericRavenTask( this, serviceId, taskElement );

        return task;
    }

    @Override
    public RavenTask createTask( TaskElement taskElement, Identification serviceId ) {
        this.put( taskElement );
        RavenTask task = this.constructTask( taskElement, serviceId );


        return task;
    }


}
