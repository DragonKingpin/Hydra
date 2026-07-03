package com.walnut.odin.atlas.deletion;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskPurgeRequest implements Pinenut {

    protected GUID    taskGuid;
    protected boolean forceOffline;
    protected long    graceTimeoutMillis = 0L;
    protected String  reason;

    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    public boolean isForceOffline() {
        return this.forceOffline;
    }

    public void setForceOffline( boolean forceOffline ) {
        this.forceOffline = forceOffline;
    }

    public long getGraceTimeoutMillis() {
        return this.graceTimeoutMillis;
    }

    public void setGraceTimeoutMillis( long graceTimeoutMillis ) {
        this.graceTimeoutMillis = graceTimeoutMillis;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason( String reason ) {
        this.reason = reason;
    }
}
