package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.ServiceRuntimeNodeEntry;

import java.util.ArrayList;
import java.util.List;

public class ServiceRuntimeNodePage implements Pinenut {

    protected List<ServiceRuntimeNodeEntry> mItems = new ArrayList<>();

    protected long mTotal;

    protected long mOffset;

    protected long mLimit;

    public ServiceRuntimeNodePage() {
    }

    public ServiceRuntimeNodePage( List<ServiceRuntimeNodeEntry> items, long nTotal, long nOffset, long nLimit ) {
        this.setItems( items );
        this.mTotal = nTotal;
        this.mOffset = nOffset;
        this.mLimit = nLimit;
    }

    public List<ServiceRuntimeNodeEntry> getItems() {
        return this.mItems;
    }

    public void setItems( List<ServiceRuntimeNodeEntry> items ) {
        if ( items == null ) {
            this.mItems = new ArrayList<>();
            return;
        }
        this.mItems = items;
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
