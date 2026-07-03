package com.walnut.odin.specific.digest;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecificPage<T> implements Pinenut {

    protected List<T> mItems = new ArrayList<>();

    protected long mnTotal;

    protected long mnOffset;

    protected long mnLimit;

    public TaskSpecificPage() {
    }

    public TaskSpecificPage( List<T> items, long nTotal, long nOffset, long nLimit ) {
        this.mItems = items == null ? new ArrayList<>() : items;
        this.mnTotal = nTotal;
        this.mnOffset = nOffset;
        this.mnLimit = nLimit;
    }

    public List<T> getItems() {
        return this.mItems;
    }

    public void setItems( List<T> items ) {
        this.mItems = items == null ? new ArrayList<>() : items;
    }

    public long getTotal() {
        return this.mnTotal;
    }

    public void setTotal( long nTotal ) {
        this.mnTotal = nTotal;
    }

    public long getOffset() {
        return this.mnOffset;
    }

    public void setOffset( long nOffset ) {
        this.mnOffset = nOffset;
    }

    public long getLimit() {
        return this.mnLimit;
    }

    public void setLimit( long nLimit ) {
        this.mnLimit = nLimit;
    }
}
