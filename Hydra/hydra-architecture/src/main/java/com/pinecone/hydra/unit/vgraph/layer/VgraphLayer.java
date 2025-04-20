package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.time.LocalDateTime;
import java.util.List;

public class VgraphLayer implements Layer{
    private String                  mszName;

    private GUID                    mGuid;

    private List<GUID>              mlHandleNodes;

    private LocalDateTime           mUpdateTime;

    private LocalDateTime           mCreateTime;

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setName(String name) {
        this.mszName = name;
    }

    @Override
    public void setGuid(GUID guid) {
        this.mGuid = guid;
    }

    @Override
    public List<GUID> getHandleNodes() {
        return this.mlHandleNodes;
    }

    @Override
    public void setHandleNode(List<GUID> handleNodes) {
        this.mlHandleNodes = handleNodes;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.mUpdateTime = updateTime;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.mCreateTime = createTime;
    }
}
