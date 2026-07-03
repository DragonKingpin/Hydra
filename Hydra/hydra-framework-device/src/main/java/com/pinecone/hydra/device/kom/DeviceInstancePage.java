package com.pinecone.hydra.device.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;

import java.util.ArrayList;
import java.util.List;

public class DeviceInstancePage implements Pinenut {

    protected List<DeviceInstanceEntry> items = new ArrayList<>();

    protected long total;

    protected long offset;

    protected long limit;

    public DeviceInstancePage() {
    }

    public DeviceInstancePage( List<DeviceInstanceEntry> items, long total, long offset, long limit ) {
        this.setItems( items );
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public List<DeviceInstanceEntry> getItems() {
        return this.items;
    }

    public void setItems( List<DeviceInstanceEntry> items ) {
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
