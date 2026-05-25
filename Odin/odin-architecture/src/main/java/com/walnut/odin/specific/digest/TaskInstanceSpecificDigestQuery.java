package com.walnut.odin.specific.digest;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.TaskInstanceQuery;

public class TaskInstanceSpecificDigestQuery extends TaskInstanceQuery {

    protected GUID mProjectGuid;

    protected GUID mBizTreeGuid;

    public GUID getProjectGuid() {
        return this.mProjectGuid;
    }

    public void setProjectGuid( GUID projectGuid ) {
        this.mProjectGuid = projectGuid;
    }

    public GUID getBizTreeGuid() {
        return this.mBizTreeGuid;
    }

    public void setBizTreeGuid( GUID bizTreeGuid ) {
        this.mBizTreeGuid = bizTreeGuid;
    }
}
