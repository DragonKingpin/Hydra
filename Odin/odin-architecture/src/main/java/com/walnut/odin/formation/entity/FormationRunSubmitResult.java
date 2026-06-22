package com.walnut.odin.formation.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FormationRunSubmitResult implements Pinenut {
    protected GUID    mRunGuid;
    protected GUID    mFormationGuid;
    protected String  mszRunStatus;
    protected long    mnTotalCount;
    protected boolean mbDispatched;

    public GUID getRunGuid() { return this.mRunGuid; }
    public void setRunGuid( GUID runGuid ) { this.mRunGuid = runGuid; }
    public GUID getFormationGuid() { return this.mFormationGuid; }
    public void setFormationGuid( GUID formationGuid ) { this.mFormationGuid = formationGuid; }
    public String getRunStatus() { return this.mszRunStatus; }
    public void setRunStatus( String runStatus ) { this.mszRunStatus = runStatus; }
    public long getTotalCount() { return this.mnTotalCount; }
    public void setTotalCount( long totalCount ) { this.mnTotalCount = totalCount; }
    public boolean isDispatched() { return this.mbDispatched; }
    public void setDispatched( boolean dispatched ) { this.mbDispatched = dispatched; }
}
