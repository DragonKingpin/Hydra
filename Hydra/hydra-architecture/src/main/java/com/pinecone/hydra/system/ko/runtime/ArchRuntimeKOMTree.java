package com.pinecone.hydra.system.ko.runtime;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.unit.trie.TrieMap;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
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

    protected String                             superiorPathScope;

    protected KernelObjectConfig                 kernelObjectConfig;


    public ArchRuntimeKOMTree( String superiorPathScope, KernelObjectConfig kernelObjectConfig ) {
        super( superiorPathScope );

        this.kernelObjectConfig  = kernelObjectConfig;
        this.mNodeTable          = new ConcurrentHashMap<>();
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
    public KernelObjectConfig getConfig() {
        return this.kernelObjectConfig;
    }

    @Override
    public String getPath( GUID guid ) {
        RuntimeTreeNode treeNode = this.mNodeTable.get( guid );
        if ( treeNode == null ) {
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
        TreeNode treeNode = this.mNodeIndex.get( path );
        if ( treeNode != null ) {
            return treeNode.getGuid();
        }
        return null;
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
    public GUID put( TreeNode treeNode ) {
        return null;
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
        return null;
    }

    @Override
    public Collection<GUID> fetchChildrenGuids( GUID guid ) {
        return null;
    }

    @Override
    public List<? extends TreeNode> fetchRoot() {
        return null;
    }

    @Override
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        return null;
    }

    @Override
    public EntityNode queryNode( String path ) {
        return null;
    }

    @Override
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        return null;
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
    }
}
