package com.pinecone.hydra.system.ko.handle;

import com.pinecone.framework.util.id.GUID;

public abstract class ArchKHandle implements KHandle {

    protected String mszTreeNodeName;

    protected GUID   mTreeNodeGuid;

    public ArchKHandle( String treeNodeName, GUID treeNodeGuid ) {
        this.mszTreeNodeName   = treeNodeName;
        this.mTreeNodeGuid     = treeNodeGuid;
    }

    protected ArchKHandle() {
        this( null, null );
    }

    public KHandle applyTreeNodeName( String szTreeNodeName ) {
        this.mszTreeNodeName = szTreeNodeName;
        return this;
    }

    public KHandle applyTreeNodeGuid( GUID treeNodeGuid ) {
        this.mTreeNodeGuid = treeNodeGuid;
        return this;
    }

    @Override
    public String getName() {
        return this.mszTreeNodeName;
    }

    @Override
    public GUID getGuid() {
        return this.mTreeNodeGuid;
    }
}
