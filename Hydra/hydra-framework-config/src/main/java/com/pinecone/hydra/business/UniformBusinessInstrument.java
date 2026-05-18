package com.pinecone.hydra.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.business.entity.ArchElementNode;
import com.pinecone.hydra.business.entity.ElementNode;
import com.pinecone.hydra.business.entity.GenericElementNode;
import com.pinecone.hydra.business.entity.GenericIdeaElement;
import com.pinecone.hydra.business.entity.GenericNodeTree;
import com.pinecone.hydra.business.entity.GenericProjectElement;
import com.pinecone.hydra.business.entity.GenericScenarioElement;
import com.pinecone.hydra.business.entity.IdeaElement;
import com.pinecone.hydra.business.entity.NodeTree;
import com.pinecone.hydra.business.entity.ProjectElement;
import com.pinecone.hydra.business.entity.ScenarioElement;
import com.pinecone.hydra.business.source.IdeaManipulator;
import com.pinecone.hydra.business.source.MasterManipulator;
import com.pinecone.hydra.business.source.NodeManipulator;
import com.pinecone.hydra.business.source.PathManipulator;
import com.pinecone.hydra.business.source.ProjectManipulator;
import com.pinecone.hydra.business.source.ScenarioManipulator;
import com.pinecone.hydra.business.source.TreeManipulator;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;

public class UniformBusinessInstrument extends ArchKOMTree implements BusinessInstrument {

    protected static final String TYPE_SCENARIO = "Scenario";
    protected static final String TYPE_PROJECT  = "Project";
    protected static final String TYPE_IDEA     = "Idea";

    protected MasterManipulator   mMasterManipulator;
    protected NodeManipulator     mNodeManipulator;
    protected TreeManipulator     mTreeManipulator;
    protected PathManipulator     mPathManipulator;
    protected ScenarioManipulator mScenarioManipulator;
    protected ProjectManipulator  mProjectManipulator;
    protected IdeaManipulator     mIdeaManipulator;

    public UniformBusinessInstrument(
            Processum superiorProcess, KOIMasterManipulator masterManipulator, BusinessInstrument parent, String name, String superiorPathScope,
            @Nullable GuidAllocator guidAllocator
    ) {
        super(
                superiorProcess, masterManipulator, BusinessInstrument.KERNEL_BUSINESS_CONFIG, parent, name, superiorPathScope,
                guidAllocator == null ? GUIDs.newGuidAllocator() : guidAllocator
        );

        this.mMasterManipulator   = (MasterManipulator) masterManipulator;
        this.mNodeManipulator     = this.mMasterManipulator.getNodeManipulator();
        this.mTreeManipulator     = this.mMasterManipulator.getTreeManipulator();
        this.mPathManipulator     = this.mMasterManipulator.getPathManipulator();
        this.mScenarioManipulator = this.mMasterManipulator.getScenarioManipulator();
        this.mProjectManipulator  = this.mMasterManipulator.getProjectManipulator();
        this.mIdeaManipulator     = this.mMasterManipulator.getIdeaManipulator();
        this.pathResolver         = new KOPathResolver( this.kernelObjectConfig );
    }

    public UniformBusinessInstrument(
            Processum superiorProcess, KOIMasterManipulator masterManipulator, BusinessInstrument parent, String name,
            @Nullable GuidAllocator guidAllocator
    ) {
        this( superiorProcess, masterManipulator, parent, name, CascadeInstrument.EmptySuperiorPathScope, guidAllocator );
    }

    public UniformBusinessInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, BusinessInstrument.class.getSimpleName(), null );
    }

    public UniformBusinessInstrument( KOIMappingDriver driver ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator() );
    }

    public UniformBusinessInstrument( KOIMappingDriver driver, BusinessInstrument parent, String name ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name, null );
    }

    @Override
    public ScenarioElement affirmScenarioElement( String szPath ) {
        return (ScenarioElement) this.affirmTreeNodeByPath( szPath, GenericScenarioElement.class );
    }

    @Override
    public ProjectElement affirmProjectElement( String szPath ) {
        return (ProjectElement) this.affirmTreeNodeByPath( szPath, GenericProjectElement.class );
    }

    @Override
    public IdeaElement affirmIdeaElement( String szPath ) {
        return (IdeaElement) this.affirmTreeNodeByPath( szPath, GenericIdeaElement.class );
    }

    protected ElementNode affirmTreeNodeByPath( String szPath, Class<? extends ElementNode > targetClass ) {
        ElementNode node = this.queryElement( szPath );
        if ( node != null ) {
            return node;
        }

        String[] parts = this.pathResolver.segmentPathParts( szPath );
        GUID parentGuid = null;
        ElementNode ret = null;
        String szCurrentPath = "";
        for ( int i = 0; i < parts.length; ++i ) {
            szCurrentPath = szCurrentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( szCurrentPath );
            if ( node == null ) {
                Class<? extends ElementNode > nodeClass = i == parts.length - 1 ? targetClass : GenericScenarioElement.class;
                node = this.newElement( nodeClass, parts[ i ] );
                this.persistElement( node, parentGuid );
                ret = node;
            }

            parentGuid = node.getGuid();
        }

        return ret == null ? this.queryElement( szPath ) : ret;
    }

    protected ElementNode newElement( Class<? extends ElementNode > nodeClass, String szName ) {
        try {
            ElementNode node = nodeClass.getDeclaredConstructor().newInstance();
            node.setName( szName );
            return node;
        }
        catch ( ReflectiveOperationException e ) {
            throw new IllegalStateException( "Cannot create business element: " + nodeClass.getName(), e );
        }
    }

    @Override
    public ScenarioElement createScenarioElement( ScenarioElement scenarioElement, GUID parentGuid ) {
        this.persistElement( scenarioElement, parentGuid );
        return this.bindInstrument( scenarioElement ).evinceScenarioElement();
    }

    @Override
    public ProjectElement createProjectElement( ProjectElement projectElement, GUID parentGuid ) {
        this.persistElement( projectElement, parentGuid );
        return this.bindInstrument( projectElement ).evinceProjectElement();
    }

    @Override
    public IdeaElement createIdeaElement( IdeaElement ideaElement, GUID parentGuid ) {
        this.persistElement( ideaElement, parentGuid );
        return this.bindInstrument( ideaElement ).evinceIdeaElement();
    }

    @Override
    public void addChild( GUID parentGuid, ElementNode child ) {
        this.assertScenarioParent( parentGuid );
        this.mTreeManipulator.updateParentGuid( child.getGuid(), parentGuid );
        this.invalidatePath( child.getGuid() );
        this.rebuildPath( child.getGuid() );
    }

    @Override
    public GUID put( TreeNode treeNode ) {
        if ( !( treeNode instanceof ElementNode ) ) {
            throw new IllegalArgumentException( "Business tree only accepts ElementNode." );
        }

        ElementNode elementNode = (ElementNode) treeNode;
        this.persistElement( elementNode, null );
        return elementNode.getGuid();
    }

    protected void persistElement( ElementNode node, GUID parentGuid ) {
        this.assertLegalParent( node, parentGuid );
        this.prepareNode( node );
        this.mNodeManipulator.insert( node );
        this.insertTypedNode( node );
        this.insertTreeNode( node.getGuid(), parentGuid );
        this.rebuildPath( node.getGuid() );
    }

    protected void insertTypedNode( ElementNode node ) {
        if ( node instanceof ScenarioElement ) {
            this.mScenarioManipulator.insert( (ScenarioElement) node );
            return;
        }
        if ( node instanceof ProjectElement ) {
            this.mProjectManipulator.insert( (ProjectElement) node );
            return;
        }
        if ( node instanceof IdeaElement ) {
            this.mIdeaManipulator.insert( (IdeaElement) node );
        }
    }

    protected void prepareNode( ElementNode node ) {
        if ( node.getGuid() == null ) {
            node.setGuid( this.getGuidAllocator().nextGUID() );
        }

        if ( node.getType() == null ) {
            node.setType( this.evalNodeType( node ) );
        }

        LocalDateTime now = LocalDateTime.now();
        if ( node.getCreateTime() == null ) {
            node.setCreateTime( now );
        }
        if ( node.getUpdateTime() == null ) {
            node.setUpdateTime( now );
        }
    }

    protected String evalNodeType( ElementNode node ) {
        if ( node instanceof ScenarioElement ) {
            return TYPE_SCENARIO;
        }
        if ( node instanceof ProjectElement ) {
            return TYPE_PROJECT;
        }
        if ( node instanceof IdeaElement ) {
            return TYPE_IDEA;
        }
        throw new IllegalArgumentException( "Unsupported business element: " + node.getClass().getName() );
    }

    protected void insertTreeNode( GUID guid, GUID parentGuid ) {
        GenericNodeTree nodeTree = new GenericNodeTree();
        nodeTree.setGuid( guid );
        nodeTree.setParentGuid( parentGuid );
        LocalDateTime now = LocalDateTime.now();
        nodeTree.setCreateTime( now );
        nodeTree.setUpdateTime( now );
        this.mTreeManipulator.insert( nodeTree );
    }

    @Override
    public ElementNode get( GUID guid ) {
        GenericElementNode node = this.mNodeManipulator.get( guid );
        if ( node == null ) {
            return null;
        }

        if ( TYPE_SCENARIO.equals( node.getType() ) ) {
            return this.mergeBase( node, this.mScenarioManipulator.get( guid ) );
        }

        if ( TYPE_PROJECT.equals( node.getType() ) ) {
            return this.mergeBase( node, this.mProjectManipulator.get( guid ) );
        }

        if ( TYPE_IDEA.equals( node.getType() ) ) {
            return this.mergeBase( node, this.mIdeaManipulator.get( guid ) );
        }

        return this.bindInstrument( node );
    }

    @Override
    public ElementNode get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public ElementNode getAsRootDepth( GUID guid ) {
        return this.get( guid );
    }

    @Override
    public boolean contains( GUID nodeGuid ) {
        return this.mNodeManipulator.get( nodeGuid ) != null;
    }

    @Override
    public void update( TreeNode treeNode ) {
        if ( !( treeNode instanceof ElementNode ) ) {
            throw new IllegalArgumentException( "Business tree only accepts ElementNode." );
        }

        ElementNode node = (ElementNode) treeNode;
        node.setUpdateTime( LocalDateTime.now() );
        this.mNodeManipulator.update( node );
        this.updateTypedNode( node );
        this.invalidatePath( node.getGuid() );
        this.rebuildPath( node.getGuid() );
    }

    protected void updateTypedNode( ElementNode node ) {
        if ( node instanceof ScenarioElement ) {
            this.mScenarioManipulator.update( (ScenarioElement) node );
            return;
        }
        if ( node instanceof ProjectElement ) {
            this.mProjectManipulator.update( (ProjectElement) node );
            return;
        }
        if ( node instanceof IdeaElement ) {
            this.mIdeaManipulator.update( (IdeaElement) node );
        }
    }

    @Override
    public void rename( GUID guid, String szName ) {
        ElementNode node = this.get( guid );
        if ( node == null ) {
            return;
        }

        node.setName( szName );
        this.update( node );
    }

    @Override
    public void remove( GUID guid ) {
        ElementNode node = this.get( guid );
        if ( node != null ) {
            this.removeTypedNode( node );
        }

        this.mNodeManipulator.remove( guid );
        this.mTreeManipulator.remove( guid );
        this.mPathManipulator.remove( guid );
    }

    protected void removeTypedNode( ElementNode node ) {
        if ( TYPE_SCENARIO.equals( node.getType() ) ) {
            this.mScenarioManipulator.remove( node.getGuid() );
            return;
        }
        if ( TYPE_PROJECT.equals( node.getType() ) ) {
            this.mProjectManipulator.remove( node.getGuid() );
            return;
        }
        if ( TYPE_IDEA.equals( node.getType() ) ) {
            this.mIdeaManipulator.remove( node.getGuid() );
        }
    }

    protected void assertLegalParent( ElementNode node, GUID parentGuid ) {
        if ( parentGuid == null ) {
            return;
        }

        this.assertScenarioParent( parentGuid );
    }

    protected void assertScenarioParent( GUID parentGuid ) {
        ElementNode parent = this.get( parentGuid );
        if ( !( parent instanceof ScenarioElement ) ) {
            throw new IllegalArgumentException( "Only ScenarioElement can contain business children." );
        }
    }

    @Override
    public ElementNode queryElement( String szPath ) {
        GUID guid = this.queryGUIDByPath( szPath );
        if ( guid == null ) {
            return null;
        }

        return this.get( guid );
    }

    @Override
    public GUID queryGUIDByPath( String szPath ) {
        return this.queryGUIDByNS( szPath, null, null );
    }

    @Override
    public GUID queryGUIDByNS( String szPath, String szBadSep, String szTargetSep ) {
        if ( szPath == null ) {
            return null;
        }

        if ( szTargetSep != null ) {
            szPath = szPath.replace( szBadSep, szTargetSep );
        }

        List<String > parts = this.pathResolver.resolvePathParts( szPath );
        String szResolvedPath = this.pathResolver.assemblePath( parts );
        return this.mPathManipulator.queryGuidByPath( szResolvedPath );
    }

    @Override
    public Object queryEntityHandleByNS( String szPath, String szBadSep, String szTargetSep ) {
        return this.queryGUIDByNS( szPath, szBadSep, szTargetSep );
    }

    @Override
    public EntityNode queryNode( String szPath ) {
        return this.queryElement( szPath );
    }

    @Override
    public TreeNode queryTreeNode( String szPath ) {
        return this.queryElement( szPath );
    }

    @Override
    public Collection<ElementNode > fetchChildren( GUID parentGuid ) {
        List<ElementNode > children = new ArrayList<>();
        for ( GUID guid : this.fetchChildrenGuids( parentGuid ) ) {
            ElementNode child = this.get( guid );
            if ( child != null ) {
                children.add( child );
            }
        }

        return children;
    }

    @Override
    public GUID fetchParentGuid( GUID guid ) {
        return this.mTreeManipulator.fetchParentGuid( guid );
    }

    @Override
    public List<GUID > fetchChildrenGuids( GUID parentGuid ) {
        return this.mTreeManipulator.fetchChildrenGuids( parentGuid );
    }

    @Override
    public List<TreeNode > getChildren( GUID guid ) {
        List<TreeNode > children = new ArrayList<>();
        children.addAll( this.fetchChildren( guid ) );
        return children;
    }

    @Override
    public List<ElementNode > fetchRoot() {
        List<ElementNode > roots = new ArrayList<>();
        for ( GUID guid : this.mTreeManipulator.fetchRootGuids() ) {
            ElementNode node = this.get( guid );
            if ( node != null ) {
                roots.add( node );
            }
        }

        return roots;
    }

    @Override
    public boolean containsChild( GUID parentGuid, String szChildName ) {
        for ( ElementNode child : this.fetchChildren( parentGuid ) ) {
            if ( szChildName.equals( child.getName() ) ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getPath( GUID guid ) {
        String szPath = this.mPathManipulator.getPath( guid );
        if ( szPath != null ) {
            return szPath;
        }

        return this.rebuildPath( guid );
    }

    @Override
    public String getFullName( GUID guid ) {
        String szPath = this.getPath( guid );
        if ( szPath == null ) {
            return null;
        }

        return szPath.replace( this.getConfig().getPathNameSeparator(), this.getConfig().getFullNameSeparator() );
    }

    protected String rebuildPath( GUID guid ) {
        ElementNode node = this.get( guid );
        if ( node == null ) {
            return null;
        }

        GUID parentGuid = this.mTreeManipulator.fetchParentGuid( guid );
        String szPath = node.getName();
        if ( parentGuid != null ) {
            String szParentPath = this.getPath( parentGuid );
            if ( szParentPath != null && !szParentPath.isEmpty() ) {
                szPath = szParentPath + this.getConfig().getPathNameSeparator() + szPath;
            }
        }

        this.mPathManipulator.remove( guid );
        int nShortPathLength = this.getConfig().getShortPathLength();
        if ( szPath.length() > nShortPathLength ) {
            this.mPathManipulator.insertLongPath( guid, szPath.substring( 0, nShortPathLength ), szPath );
        }
        else {
            this.mPathManipulator.insert( guid, szPath );
        }

        return szPath;
    }

    protected void invalidatePath( GUID guid ) {
        this.mPathManipulator.remove( guid );
        for ( GUID childGuid : this.fetchChildrenGuids( guid ) ) {
            this.invalidatePath( childGuid );
        }
    }

    protected <T extends ElementNode> T bindInstrument( T node ) {
        if ( node instanceof ArchElementNode ) {
            ( (ArchElementNode) node ).setBusinessInstrument( this );
        }

        return node;
    }

    protected ElementNode mergeBase( GenericElementNode baseNode, ElementNode typedNode ) {
        if ( typedNode == null ) {
            return this.bindInstrument( baseNode );
        }

        typedNode.setGuid( baseNode.getGuid() );
        typedNode.setType( baseNode.getType() );
        typedNode.setName( baseNode.getName() );
        typedNode.setCode( baseNode.getCode() );
        typedNode.setDescription( baseNode.getDescription() );
        typedNode.setOwner( baseNode.getOwner() );
        typedNode.setExtraInformation( baseNode.getExtraInformation() );
        typedNode.setCreateTime( baseNode.getCreateTime() );
        typedNode.setUpdateTime( baseNode.getUpdateTime() );
        return this.bindInstrument( typedNode );
    }
}
