package com.pinecone.hydra.atlas.entity;

import com.pinecone.framework.util.id.GUID;

import java.util.List;

public class TaskAtlasNode implements TaskGraphNode {
    private GUID guid;

    private String name;

    private List<GUID> parentIds;

    public TaskAtlasNode(){
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public GUID getId() {
        return this.guid;
    }

    @Override
    public void setId(GUID guid) {
        this.guid = guid;
    }

    @Override
    public List<GUID> getParentIds() {
        return this.parentIds;
    }

    @Override
    public void setParentIds(List<GUID> parentIds) {
        this.parentIds = parentIds;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
