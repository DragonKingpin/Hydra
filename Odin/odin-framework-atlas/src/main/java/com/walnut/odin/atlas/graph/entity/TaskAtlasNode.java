package com.walnut.odin.atlas.graph.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

import java.util.List;

public class TaskAtlasNode implements TaskGraphNode {
    private long            enumId;

    private GUID            guid;

    private GUID            taskGuid;

    private String          name;

    private List<GUID>      parentIds;

    private String          description;

    private boolean         source;

    public TaskAtlasNode(){
    }

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
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
    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    @Override
    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
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

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    public boolean isSource() {
        return this.source;
    }

    public void setSource( boolean source ) {
        this.source = source;
    }
}
