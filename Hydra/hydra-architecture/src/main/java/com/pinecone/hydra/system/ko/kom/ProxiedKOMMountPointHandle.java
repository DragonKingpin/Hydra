package com.pinecone.hydra.system.ko.kom;

import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.handle.ArchKHandle;
import com.pinecone.hydra.system.ko.handle.KOMMountPointHandle;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class ProxiedKOMMountPointHandle extends ArchKHandle implements KOMMountPointHandle {

    protected KOMInstrument mWarpedInstrument;

    public ProxiedKOMMountPointHandle( String treeNodeName, GUID treeNodeGuid, KOMInstrument warpedInstrument ) {
        super( treeNodeName, treeNodeGuid );
        this.mWarpedInstrument = warpedInstrument;
    }

    @Override
    public KOMInstrument revealWrapped() {
        return this.mWarpedInstrument;
    }

    @Override
    public KOMInstrument parent() {
        return this.mWarpedInstrument.parent();
    }

    @Override
    public void setParent( CascadeInstrument parent ) {
        this.mWarpedInstrument.setParent( parent );
    }

    @Override
    public Namespace getTargetingName() {
        return this.mWarpedInstrument.getTargetingName();
    }

    @Override
    public void setTargetingName( Namespace name ) {
        this.mWarpedInstrument.setTargetingName( name );
    }

    @Override
    public String getSuperiorPathScope() {
        return this.mWarpedInstrument.getSuperiorPathScope();
    }

    @Override
    public void applySuperiorPathScope( String superiorPathScope ) {
        this.mWarpedInstrument.applySuperiorPathScope( superiorPathScope );
    }

    @Override
    public String getPath( GUID objectGuid ) {
        return this.mWarpedInstrument.getPath( objectGuid );
    }

    @Override
    public String querySystemKernelObjectPath( GUID objectGuid ) {
        return this.mWarpedInstrument.querySystemKernelObjectPath( objectGuid );
    }

    @Override
    public String getFullName( GUID objectGuid ) {
        return this.mWarpedInstrument.getFullName( objectGuid );
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.mWarpedInstrument.queryGUIDByPath( path );
    }

    @Override
    public GUID queryGUIDByFN( String fullName ) {
        return this.mWarpedInstrument.queryGUIDByFN( fullName );
    }

    @Override
    public boolean contains( GUID nodeGuid ) {
        return this.mWarpedInstrument.contains( nodeGuid );
    }

    @Override
    public GUID put( TreeNode treeNode ) {
        return this.mWarpedInstrument.put( treeNode );
    }

    @Override
    public TreeNode get( GUID objectGuid ) {
        return this.mWarpedInstrument.get( objectGuid );
    }

    @Override
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        return this.mWarpedInstrument.queryGUIDByNS( path, szBadSep, szTargetSep );
    }

    @Override
    public TreeNode get( GUID guid, int depth ) {
        return this.mWarpedInstrument.get( guid, depth );
    }

    @Override
    public TreeNode getAsRootDepth( GUID guid ) {
        return this.mWarpedInstrument.getAsRootDepth( guid );
    }

    @Override
    public void remove( GUID guid ) {
        this.mWarpedInstrument.remove( guid );
    }

    @Override
    public void remove( String path ) {
        this.mWarpedInstrument.remove( path );
    }

    @Override
    public Collection<TreeNode> getChildren( GUID guid ) {
        return this.mWarpedInstrument.getChildren( guid );
    }

    @Override
    public Collection<GUID> fetchChildrenGuids( GUID guid ) {
        return this.mWarpedInstrument.fetchChildrenGuids( guid );
    }

    @Override
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        return this.mWarpedInstrument.queryEntityHandleByNS( path, szBadSep, szTargetSep );
    }

    @Override
    public EntityNode queryNode( String path ) {
        return this.mWarpedInstrument.queryNode( path );
    }

    @Override
    public TreeNode queryTreeNode( String path ) {
        return this.mWarpedInstrument.queryTreeNode( path );
    }

    @Override
    public List<? extends TreeNode> fetchRoot() {
        return this.mWarpedInstrument.fetchRoot();
    }

    @Override
    public void rename( GUID guid, String name ) {
        this.mWarpedInstrument.rename( guid, name );
    }

    @Override
    public Processum getSuperiorProcess() {
        return this.mWarpedInstrument.getSuperiorProcess();
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mWarpedInstrument.getGuidAllocator();
    }

    @Override
    public ImperialTree getMasterTrieTree() {
        return this.mWarpedInstrument.getMasterTrieTree();
    }

    @Override
    public KernelObjectConfig getConfig() {
        return this.mWarpedInstrument.getConfig();
    }

    @Override
    public String getName() {
        return this.mszTreeNodeName;
    }

    @Override
    public GUID getGuid() {
        return this.mTreeNodeGuid;
    }

    @Override
    public String toJSONString() {
        return this.mWarpedInstrument.toJSONString();
    }

    @Override
    public String toString() {
        return this.mWarpedInstrument.toString();
    }
}