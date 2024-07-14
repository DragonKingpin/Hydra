package com.pinecone.framework.util.json;

import com.pinecone.framework.system.prototype.OverridableFamily;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JPlusContext implements OverridableFamily, Cloneable {
    protected List<Object > mGlobalScopes;
    protected Object        mParent;
    protected Object        mThisScope;
    protected Object        mRoot;
    protected Path[]        mParentPaths;
    protected boolean       mOverriddenAffinity;

    public JPlusContext() {
        this( null, null, null, new Path[0] );
    }

    public JPlusContext( Object parent, Object thisScope, Object root, Path[] parentPaths ) {
        this( new ArrayList<>(), parent, thisScope, root, parentPaths );
    }

    public JPlusContext( List<Object > globalScopes, Object parent, Object thisScope, Object root, Path[] parentPaths ) {
        this.mGlobalScopes  = globalScopes;
        this.mParent        = parent;
        this.mThisScope     = thisScope;
        this.mRoot          = root;
        this.mParentPaths   = parentPaths;
    }

    public JPlusContext( Object globalScope, Object parent, Object thisScope, Object root, Path[] parentPaths ) {
        this( parent, thisScope, root, parentPaths );
        this.addGlobalScope( globalScope );
    }

    @Override
    public List<Object > getGlobalScopes() {
        return this.mGlobalScopes;
    }

    @Override
    public Object parent() {
        return this.mParent;
    }

    @Override
    public Object thisScope() {
        return this.mThisScope;
    }

    @Override
    public Object root() {
        return this.mRoot;
    }

    @Override
    public Path[] getParentPaths() {
        return this.mParentPaths;
    }

    @Override
    public JPlusContext setGlobalScopes(List<Object > globalScopes) {
        this.mGlobalScopes = globalScopes;
        return this;
    }

    @Override
    public JPlusContext setParent(Object parent) {
        this.mParent = parent;
        return this;
    }

    @Override
    public JPlusContext setThisScope(Object thisScope) {
        this.mThisScope = thisScope;
        return this;
    }

    @Override
    public JPlusContext setRoot(Object root) {
        this.mRoot = root;
        return this;
    }

    @Override
    public JPlusContext setParentPaths(Path[] parentPaths) {
        this.mParentPaths = parentPaths;
        return this;
    }

    @Override
    public JPlusContext addParentPath( Path newPath ) {
        int length = this.mParentPaths.length;
        Path[] newParentPaths = Arrays.copyOf( this.mParentPaths, length + 1 );
        newParentPaths[length] = newPath;
        this.mParentPaths = newParentPaths;
        return this;
    }

    @Override
    public JPlusContext addGlobalScope( Object scope ) {
        this.getGlobalScopes().add( scope );
        return this;
    }

    /**
     * isOverriddenAffinity
     * @return if $this, $super and $root, are forced overridden by global scope.
     */
    @Override
    public boolean isOverriddenAffinity() {
        return this.mOverriddenAffinity;
    }

    @Override
    public void setOverriddenAffinity( boolean overrideAffinity ) {
        this.mOverriddenAffinity = overrideAffinity;
    }

    @Override
    public JPlusContext clone() {
        JPlusContext clone;
        try {
            clone = (JPlusContext) super.clone();
        }
        catch ( CloneNotSupportedException e ) {
            throw new InternalError(e);
        }

        return clone;
    }
}
