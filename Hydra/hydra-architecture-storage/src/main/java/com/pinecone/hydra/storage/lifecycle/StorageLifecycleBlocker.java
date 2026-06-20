package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.List;

public class StorageLifecycleBlocker implements Pinenut {
    protected String                           mszCode;
    protected String                           mszMessageKey;
    protected String                           mszMessage;
    protected long                             mnCount;
    protected boolean                          mbHasMore;
    protected final List<StorageLifecycleDependency> mDependencies = new ArrayList<>();

    public StorageLifecycleBlocker() {
    }

    public StorageLifecycleBlocker( String code, String message, long count ) {
        this.mszCode = code;
        this.mszMessageKey = messageKeyOf( code );
        this.mszMessage = message;
        this.mnCount = count;
    }

    public String getCode() {
        return this.mszCode;
    }

    public void setCode( String code ) {
        this.mszCode = code;
        if ( this.mszMessageKey == null ) {
            this.mszMessageKey = messageKeyOf( code );
        }
    }

    public String getMessageKey() {
        return this.mszMessageKey;
    }

    public void setMessageKey( String messageKey ) {
        this.mszMessageKey = messageKey;
    }

    public String getMessage() {
        return this.mszMessage;
    }

    public void setMessage( String message ) {
        this.mszMessage = message;
    }

    public long getCount() {
        return this.mnCount;
    }

    public void setCount( long count ) {
        this.mnCount = count;
    }

    public boolean isHasMore() {
        return this.mbHasMore;
    }

    public void setHasMore( boolean hasMore ) {
        this.mbHasMore = hasMore;
    }

    public List<StorageLifecycleDependency> getDependencies() {
        return this.mDependencies;
    }

    public void addDependency( StorageLifecycleDependency dependency ) {
        if ( dependency != null ) {
            this.mDependencies.add( dependency );
        }
    }

    protected static String messageKeyOf( String code ) {
        return code == null ? null : "storage.lifecycle.issue." + code;
    }
}
