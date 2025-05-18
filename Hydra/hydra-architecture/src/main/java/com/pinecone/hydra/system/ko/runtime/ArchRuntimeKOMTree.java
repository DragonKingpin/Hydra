package com.pinecone.hydra.system.ko.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.unit.Units;
import com.pinecone.framework.unit.trie.TrieMap;
import com.pinecone.framework.unit.trie.UniTrieMaptron;
import com.pinecone.framework.util.CollectionUtils;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.handle.ObjectTreeAddressingSectionHandle;
import com.pinecone.hydra.system.ko.handle.ObjectTreeGUIDAddressingSectionHandle;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.system.ko.kom.ProxiedKOMMountPointHandle;
import com.pinecone.hydra.unit.imperium.ArchUniformInstitutionalizedInstrument;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

public abstract class ArchRuntimeKOMTree extends ArchUniformInstitutionalizedInstrument implements RuntimeInstrument {
    protected Namespace                          mThisNamespace;
    protected KOMInstrument                      mParentInstrument;

    protected TrieMap<String, TreeNode  >        mNodeIndex;
    protected Map<GUID, RuntimeTreeNode >        mNodeTable;

    protected Hydrarum                           hydrarum;

    protected Processum                          superiorProcess;

    protected GuidAllocator                      guidAllocator;

    protected DynamicFactory                     dynamicFactory;

    protected KernelObjectConfig                 kernelObjectConfig;


    public ArchRuntimeKOMTree( String superiorPathScope, KernelObjectConfig kernelObjectConfig ) {
        super( superiorPathScope );

        this.kernelObjectConfig  = kernelObjectConfig;
        this.mNodeIndex          = new UniTrieMaptron<>( ConcurrentHashMap::new );
        this.mNodeTable          = new ConcurrentHashMap<>();
        this.guidAllocator       = new GenericGuidAllocator();
    }

    //************************************** CascadeInstrument **************************************
    @Override
    public KOMInstrument parent() {
        return this.mParentInstrument;
    }

    @Override
    public Processum getSuperiorProcess() {
        return this.superiorProcess;
    }

    @Override
    public void setParent( CascadeInstrument parent ) {
        this.mParentInstrument = (KOMInstrument) parent;
    }

    @Override
    public Namespace getTargetingName() {
        return this.mThisNamespace;
    }

    @Override
    public void setTargetingName( Namespace name ) {
        this.mThisNamespace = name;
    }

    //************************************** CascadeInstrument End **************************************


    @Override
    public GuidAllocator getGuidAllocator() {
        return this.guidAllocator;
    }

    @Override
    public String getSuperiorPathScope() {
        return this.superiorPathScope;
    }

    @Override
    public void applySuperiorPathScope( String superiorPathScope ) {
        this.superiorPathScope = superiorPathScope;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<TreeNode> fetchTreeNodes() {
        return (Collection) this.mNodeTable.values();
    }


    @Override
    public KOMInstrument implicated( GUID objectGuid ) {
        RuntimeTreeNode treeNode = this.mNodeTable.get( objectGuid );
        if ( treeNode == null ) {
            for( RuntimeTreeNode node : this.mNodeTable.values() ) {
                if ( node.treeNode instanceof KOMInstrument ) {
                    KOMInstrument instrument = (KOMInstrument) node.treeNode;
                    TreeNode sn = instrument.get( objectGuid );
                    if ( sn != null ) {
                        return instrument;
                    }
                }
            }

            return null;
        }

        if ( treeNode instanceof KOMInstrument ) {
            return (KOMInstrument) treeNode;
        }
        return null;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return this.kernelObjectConfig;
    }

    @Override
    public String getPath( GUID guid ) {
        RuntimeTreeNode treeNode = this.mNodeTable.get( guid );
        if ( treeNode == null ) {
            for( RuntimeTreeNode node : this.mNodeTable.values() ) {
                if ( node.treeNode instanceof KOMInstrument ) {
                    KOMInstrument instrument = (KOMInstrument) node.treeNode;
                    String path = instrument.getPath( guid );
                    if ( StringUtils.isNoneEmpty( path ) ) {
                        return path;
                    }
                }
            }

            return null;
        }
        return treeNode.getPath();
    }

    @Override
    public String getFullName( GUID guid ) {
        return this.getPath( guid );
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.queryGUIDByPathForward(path);
    }

    protected GUID queryGUIDByPathBackward( String path ) {
        TreeNode treeNode = this.mNodeIndex.get( path );
        if ( treeNode != null ) {
            return treeNode.getGuid();
        }

        String[] split = path.split(this.kernelObjectConfig.getPathNameSepRegex());
        for( int i = split.length - 2; i >= 0; --i ) {
            TreeNode node = this.mNodeIndex.get( this.concatenateFullPathBySegments(split, 0, i) );
            if( node instanceof RuntimeTreeNode ) {
                RuntimeTreeNode rtn = (RuntimeTreeNode)node;
                if ( rtn instanceof ObjectTreeGUIDAddressingSectionHandle ) {
                    ObjectTreeGUIDAddressingSectionHandle pointHandle = (ObjectTreeGUIDAddressingSectionHandle) rtn.treeNode;
                    GUID guid = pointHandle.queryGUIDByPath( this.concatenateFullPathBySegments(split, i + 1, split.length - 1) );
                    //this.mNodeIndex.put( path, pointHandle.get(guid) );
                    return guid;
                }
            }
        }
        return null;
    }

    protected GUID queryGUIDByPathForward( String path ) {
        TreeNode treeNode = this.mNodeIndex.get( path );
        if ( treeNode != null ) {
            return treeNode.getGuid();
        }

        String[] split = path.split(this.kernelObjectConfig.getPathNameSeparator());
        for( int i = 0; i < split.length; ++i ) {
            TreeNode node = this.mNodeIndex.get( this.concatenateFullPathBySegments(split, 0, i) );
            if( node instanceof RuntimeTreeNode ) {
                RuntimeTreeNode rtn = (RuntimeTreeNode)node;
                if ( rtn instanceof ObjectTreeGUIDAddressingSectionHandle ) {
                    ObjectTreeGUIDAddressingSectionHandle pointHandle = (ObjectTreeGUIDAddressingSectionHandle) rtn.treeNode;
                    GUID guid = pointHandle.queryGUIDByPath( this.concatenateFullPathBySegments(split, i + 1, split.length - 1) );
                    //this.mNodeIndex.put( path, pointHandle.get(guid) );
                    return guid;
                }
            }
        }
        return null;
    }

    protected String concatenateFullPathBySegments( String[] segments, int start, int end ) {
        StringBuilder stringBuilder = new StringBuilder();
        for( int i = start; i <= end; ++i ) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(this.kernelObjectConfig.getPathNameSeparator());
            }
            stringBuilder.append( segments[ i ] );
        }
        return stringBuilder.toString();
    }

    @Override
    public GUID queryGUIDByFN( String fullName ) {
        return this.queryGUIDByPath( fullName );
    }

    @Override
    public boolean contains( GUID nodeGuid ) {
        return this.mNodeTable.containsKey( nodeGuid );
    }

    @Override
    public GUID put( TreeNode treeNode ) throws IllegalArgumentException {
        RuntimeTreeNode runtimeTreeNode;
        if ( treeNode instanceof RuntimeTreeNode ) {
            runtimeTreeNode = (RuntimeTreeNode) treeNode;
        }
        else {
            throw new IllegalArgumentException( "TreeNode which been putted should be `RuntimeTreeNode`." );
        }
        this.mNodeTable.put( treeNode.getGuid(), runtimeTreeNode );
        return treeNode.getGuid();
    }

    @Override
    public TreeNode add( String mountPointPath, TreeNode that ) {
        RuntimeTreeNode runtimeTreeNode;
        if ( that instanceof RuntimeTreeNode ) {
            runtimeTreeNode = (RuntimeTreeNode) that;
        }
        else {
            runtimeTreeNode = new RuntimeTreeNode( that, mountPointPath );
        }

        this.mNodeTable.put( that.getGuid(), runtimeTreeNode );
        this.mNodeIndex.put( mountPointPath, runtimeTreeNode );
        return that;
    }


    @Override
    public TreeNode get( GUID guid ) {
        return this.mNodeTable.get( guid );
    }

    @Override
    public TreeNode get( GUID guid, int depth ) {
        return this.mNodeTable.get( guid );
    }

    @Override
    public TreeNode getAsRootDepth( GUID guid ) {
        return this.mNodeTable.get( guid );
    }

    @Override
    public void remove( GUID guid ) {
        RuntimeTreeNode treeNode = this.mNodeTable.get( guid );
        if ( treeNode != null ) {
            this.mNodeIndex.remove( treeNode.getPath() );
            this.mNodeTable.remove( guid );
        }
    }

    @Override
    public void remove( String path ) {
        GUID guid = this.queryGUIDByPath( path );
        if ( guid != null ) {
            this.remove( guid );
        }
    }

    @Override
    public void rename( GUID guid, String name ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<TreeNode> getChildren( GUID guid ) {
        for( RuntimeTreeNode node : this.mNodeTable.values() ) {
            if ( node.treeNode instanceof KOMInstrument ) {
                KOMInstrument instrument = (KOMInstrument) node.treeNode;
                Collection<TreeNode > cs = instrument.getChildren( guid );
                if ( CollectionUtils.isNoneEmpty( cs ) ) {
                    return cs;
                }
            }
        }
        return Units.emptyList();
    }

    @Override
    public Collection<GUID> fetchChildrenGuids( GUID guid ) {
        for( RuntimeTreeNode node : this.mNodeTable.values() ) {
            if ( node.treeNode instanceof KOMInstrument ) {
                KOMInstrument instrument = (KOMInstrument) node.treeNode;
                Collection<GUID> cs = instrument.fetchChildrenGuids( guid );
                if ( CollectionUtils.isNoneEmpty( cs ) ) {
                    return cs;
                }
            }
        }
        return Units.emptyList();
    }

    @Override
    public List<? extends TreeNode> fetchRoot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        TreeNode treeNode = this.mNodeIndex.get( path );
        if ( treeNode != null ) {
            return treeNode;
        }

        String[] split = path.split( this.kernelObjectConfig.getPathNameSeparator() );
        for( int i = 0; i < split.length; ++i ) {
            TreeNode node = this.mNodeIndex.get( this.concatenateFullPathBySegments(split, 0, i) );
            if( node instanceof RuntimeTreeNode ) {
                RuntimeTreeNode rtn = (RuntimeTreeNode)node;
                if ( rtn instanceof ObjectTreeAddressingSectionHandle ) {
                    ObjectTreeAddressingSectionHandle pointHandle = (ObjectTreeAddressingSectionHandle) rtn.treeNode;
                    EntityNode entityNode = pointHandle.queryNode( this.concatenateFullPathBySegments(split, i + 1, split.length - 1) );
                    return entityNode;
                }
            }
        }
        return null;
    }

    @Override
    public EntityNode queryNode( String path ) {
        Object o = this.queryEntityHandleByNS( path, null, null );
        if( o instanceof EntityNode ) {
            return (EntityNode) o;
        }
        return null;
    }

    @Override
    public TreeNode queryTreeNode( String path ) {
        Object o = this.queryEntityHandleByNS( path, null, null );
        if( o instanceof TreeNode ) {
            return (TreeNode) o;
        }
        // Runtime KOM shouldn`t be GUID.
//        else if( o instanceof GUID ) {
//            return this.get( (GUID) o );
//        }
        return null;
    }

    @Override
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }
        return this.queryGUIDByPath( path );
    }

    @Override
    public String querySystemKernelObjectPath( GUID objectGuid ) {
        String thisScopePath = this.getPath( objectGuid );
        if ( thisScopePath == null ) {
            return null;
        }

        KOMInstrument imp = this.implicated( objectGuid );
        if ( imp != null ) {
            thisScopePath = imp.querySystemKernelObjectPath( objectGuid );
        }

        return this.getSuperiorPathScope() + this.getConfig().getPathNameSeparator() + thisScopePath;
    }

    @Override
    public ImperialTree getMasterTrieTree() {
        return null;
    }

    static class RuntimeTreeNode implements TreeNode {
        private TreeNode treeNode;

        private String   path;

        public RuntimeTreeNode( TreeNode treeNode, String path ) {
            this.treeNode = treeNode;
            this.path     = path;
        }

        @Override
        public String getName() {
            return this.treeNode.getName();
        }

        @Override
        public GUID getGuid() {
            return this.treeNode.getGuid();
        }

        public TreeNode getTreeNode() {
            return this.treeNode;
        }

        public String getPath() {
            return this.path;
        }

        @Override
        public String toJSONString() {
            return this.treeNode.toJSONString();
        }

        @Override
        public String toString() {
            return this.treeNode.toString();
        }
    }
}
