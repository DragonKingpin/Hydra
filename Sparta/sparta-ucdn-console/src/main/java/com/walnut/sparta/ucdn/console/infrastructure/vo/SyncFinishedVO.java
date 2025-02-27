package com.walnut.sparta.ucdn.console.infrastructure.vo;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

public class SyncFinishedVO implements Pinenut {
    private String path;

    private String serviceId;

    private int syncState;

    public SyncFinishedVO(){}

    public SyncFinishedVO( String path, String serviceId, int syncState ){
        this.path = path;
        this.serviceId = serviceId;
        this.syncState = syncState;
    }


    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getSyncState() {
        return this.syncState;
    }

    public void setSyncState(int syncState) {
        this.syncState = syncState;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
