package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class LayerGraphHandle implements Pinenut {
    private String                  mszName;

    private GUID                    mGuid;

    private GUID                    mHandleNodeGuid;

    private LocalDateTime           mUpdateTime;

    private LocalDateTime           mCreateTime;

    public String getName() {
        return this.mszName;
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public GUID getHandleNodeGuid() {
        return this.mHandleNodeGuid;
    }

    public void setHandleNodeGuid(GUID handleNode) {
        this.mHandleNodeGuid = handleNode;
    }

    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.mUpdateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.mCreateTime = createTime;
    }
}
