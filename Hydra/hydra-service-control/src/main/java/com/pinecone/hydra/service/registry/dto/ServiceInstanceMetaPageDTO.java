package com.pinecone.hydra.service.registry.dto;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceInstanceMetaPageDTO implements Pinenut {

    private List<ServiceInstanceMetaDTO> items = new ArrayList<>();

    private long total;

    private long offset;

    private long limit;

    public List<ServiceInstanceMetaDTO> getItems() {
        return this.items;
    }

    public void setItems( List<ServiceInstanceMetaDTO> items ) {
        if ( items == null ) {
            this.items = new ArrayList<>();
            return;
        }

        this.items = items;
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
