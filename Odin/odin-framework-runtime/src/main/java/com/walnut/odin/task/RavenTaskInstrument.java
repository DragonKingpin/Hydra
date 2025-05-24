package com.walnut.odin.task;

import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.odin.task.entity.GenericRavenTaskElement;
import com.walnut.odin.task.entity.RavenTaskElement;
import com.walnut.odin.task.entity.RavenTaskMeta;
import com.walnut.odin.task.service.CategoryService;
import com.walnut.odin.task.service.RavenCategoryService;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.TaskExMetaManipulator;
import com.walnut.odin.task.system.TaskPathInvalidException;

public class RavenTaskInstrument implements CentralizedTaskInstrument {
    protected RavenTaskMasterManipulator ravenTaskMasterManipulator;

    protected UniformTaskInstrument      uniformTaskInstrument;

    protected CategoryService            categoryService;

    protected TaskExMetaManipulator      taskExMetaManipulator;

    protected void overrideTaskInstrument( Processum superiorProcess, TaskMappingDriver driver, TaskInstrument parent, String name, @Nullable GuidAllocator guidAllocator ) {
        this.uniformTaskInstrument      = new UniformTaskInstrument( superiorProcess, driver.getMasterManipulator(), parent, name, guidAllocator ) {
            @Override
            public RavenTaskElement affirmTask( String path ) {
                TaskElement taskElement           = super.affirmTask( path );
                if ( taskElement == null ) {
                    return null;
                }

                return RavenTaskInstrument.this.transformTaskElement( taskElement );
            }

            @Override
            public ElementNode queryElement( String path ) {
                ElementNode proto = super.queryElement( path );
                if ( proto instanceof TaskElement ) {
                    return RavenTaskInstrument.this.transformTaskElement( (TaskElement) proto );
                }

                return proto;
            }

            @Override
            public TaskTreeNode get( GUID guid ) {
                TaskTreeNode treeNode = super.get( guid );
                return RavenTaskInstrument.this.transformTreeNode( treeNode );
            }

            @Override
            public TreeNode get( GUID guid, int depth ) {
                TreeNode treeNode = super.get( guid, depth );
                return RavenTaskInstrument.this.transformTreeNode( (TaskTreeNode) treeNode );
            }

            @Override
            public TreeNode getAsRootDepth( GUID guid ) {
                TreeNode treeNode =  super.getAsRootDepth( guid );
                return RavenTaskInstrument.this.transformTreeNode( (TaskTreeNode) treeNode );
            }
        };
    }

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, TaskInstrument parent, String name, @Nullable GuidAllocator guidAllocator ) {
        this.ravenTaskMasterManipulator = (RavenTaskMasterManipulator) masterManipulator;
        TaskMappingDriver driver        = this.ravenTaskMasterManipulator.getTaskMappingDriver();
        this.overrideTaskInstrument     ( superiorProcess, driver, parent, name, guidAllocator );

        this.categoryService            = new RavenCategoryService( this );
        this.taskExMetaManipulator      = this.ravenTaskMasterManipulator.getTaskExMetaManipulator();
    }

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, CentralizedTaskInstrument.class.getSimpleName(), null );
    }

    public RavenTaskInstrument( KOIMappingDriver driver, CentralizedTaskInstrument parent, String name ){
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name, null );
    }

    public RavenTaskInstrument( KOIMappingDriver driver ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator() );
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








    protected RavenTaskElement transformTaskElement ( TaskElement that ) {
        RavenTaskMeta ravenTaskMeta       = this.taskExMetaManipulator.getTaskExMeta( that.getGuid(), null );
        RavenTaskElement ravenTaskElement = new GenericRavenTaskElement( that, ravenTaskMeta );
        if ( ravenTaskMeta != null ) {
            ravenTaskMeta.setKernelMeta( ravenTaskElement );
        }

        return ravenTaskElement;
    }

    protected TaskTreeNode transformTreeNode( TaskTreeNode that ) {
        if ( that instanceof TaskElement ) {
            return this.transformTaskElement( (TaskElement) that );
        }

        return that;
    }

    @Override
    public JobElement affirmJob( String path ) {
        return this.uniformTaskInstrument.affirmJob( path );
    }

    @Override
    public Namespace affirmNamespace( String path ) {
        return this.uniformTaskInstrument.affirmNamespace( path );
    }

    @Override
    public RavenTaskElement affirmTask( String path ) {
        return (RavenTaskElement) this.uniformTaskInstrument.affirmTask( path );
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


}
