package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.List;

public class TaskInstancePage implements Pinenut {

    protected List<InstanceEntry> mItems = new ArrayList<>();

    protected long mTotal;

    protected long mOffset;

    protected long mLimit;

    public TaskInstancePage() {
    }

    public TaskInstancePage( List<InstanceEntry> items, long nTotal, long nOffset, long nLimit ) {
        this.mItems = items == null ? new ArrayList<>() : items;
        this.mTotal = nTotal;
        this.mOffset = nOffset;
        this.mLimit = nLimit;
    }

    public List<InstanceEntry> getItems() {
        return this.mItems;
    }

    public void setItems( List<InstanceEntry> items ) {
        this.mItems = items == null ? new ArrayList<>() : items;
    }

    public long getTotal() {
        return this.mTotal;
    }

    public void setTotal( long nTotal ) {
        this.mTotal = nTotal;
    }

    public long getOffset() {
        return this.mOffset;
    }

    public void setOffset( long nOffset ) {
        this.mOffset = nOffset;
    }

    public long getLimit() {
        return this.mLimit;
    }

    public void setLimit( long nLimit ) {
        this.mLimit = nLimit;
    }
}
