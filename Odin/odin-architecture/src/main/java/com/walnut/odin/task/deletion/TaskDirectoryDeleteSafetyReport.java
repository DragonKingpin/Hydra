package com.walnut.odin.task.deletion;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskDirectoryDeleteSafetyReport implements Pinenut {

    protected GUID                           guid;
    protected String                         path;
    protected String                         name;
    protected String                         type;
    protected int                            childCount;
    protected List<TaskDirectoryDeleteChild> childrenPreview = new ArrayList<>();
    protected boolean                        deletable;
    protected String                         message;

    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public int getChildCount() {
        return this.childCount;
    }

    public void setChildCount( int childCount ) {
        this.childCount = childCount;
    }

    public List<TaskDirectoryDeleteChild> getChildrenPreview() {
        return this.childrenPreview;
    }

    public void setChildrenPreview( List<TaskDirectoryDeleteChild> childrenPreview ) {
        this.childrenPreview = childrenPreview;
    }

    public boolean isDeletable() {
        return this.deletable;
    }

    public void setDeletable( boolean deletable ) {
        this.deletable = deletable;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }
}
