package com.walnut.odin.specific.digest;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskSpecificDigestQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long mnOffset = 0;

    protected long mnLimit = DEFAULT_LIMIT;

    protected String mszKeyword;

    protected GUID mTaskGuid;

    protected GUID mProjectGuid;

    protected Boolean mEnable;

    protected String mszTaskType;

    protected String mszScheduleCycle;

    protected String mszScheduleType;

    public long getOffset() {
        return this.mnOffset;
    }

    public void setOffset( long nOffset ) {
        this.mnOffset = Math.max( 0, nOffset );
    }

    public long getLimit() {
        return this.mnLimit;
    }

    public void setLimit( long nLimit ) {
        if ( nLimit <= 0 ) {
            this.mnLimit = DEFAULT_LIMIT;
            return;
        }
        this.mnLimit = Math.min( nLimit, MAX_LIMIT );
    }

    public String getKeyword() {
        return this.mszKeyword;
    }

    public void setKeyword( String szKeyword ) {
        this.mszKeyword = szKeyword;
    }

    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    public GUID getProjectGuid() {
        return this.mProjectGuid;
    }

    public void setProjectGuid( GUID projectGuid ) {
        this.mProjectGuid = projectGuid;
    }

    public Boolean getEnable() {
        return this.mEnable;
    }

    public void setEnable( Boolean enable ) {
        this.mEnable = enable;
    }

    public String getTaskType() {
        return this.mszTaskType;
    }

    public void setTaskType( String szTaskType ) {
        this.mszTaskType = szTaskType;
    }

    public String getScheduleCycle() {
        return this.mszScheduleCycle;
    }

    public void setScheduleCycle( String szScheduleCycle ) {
        this.mszScheduleCycle = szScheduleCycle;
    }

    public String getScheduleType() {
        return this.mszScheduleType;
    }

    public void setScheduleType( String szScheduleType ) {
        this.mszScheduleType = szScheduleType;
    }
}
