package com.pinecone.hydra.device.kom;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.kom.digest.DeviceElementDigest;

public class DeviceElementDigestPage implements Pinenut {

    protected List<DeviceElementDigest> items = new ArrayList<>();

    protected long total;

    protected long offset;

    protected long limit;

    public DeviceElementDigestPage() {
    }

    public DeviceElementDigestPage( List<DeviceElementDigest> items, long total, long offset, long limit ) {
        this.setItems( items );
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public List<DeviceElementDigest> getItems() {
        return this.items;
    }

    public void setItems( List<DeviceElementDigest> items ) {
        this.items = items == null ? new ArrayList<>() : items;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal( long total ) {
        this.total = total;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset( long offset ) {
        this.offset = offset;
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit( long limit ) {
        this.limit = limit;
    }
}
