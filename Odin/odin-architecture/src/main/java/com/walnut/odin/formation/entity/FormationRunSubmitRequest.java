package com.walnut.odin.formation.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FormationRunSubmitRequest implements Pinenut {
    protected GUID    mFormationGuid;
    protected String  mszStrategyType;
    protected long    mnPageSize;
    protected long    mnFrameSize;
    protected long    mnWindowSize;
    protected long    mnInflightLimit;
    protected boolean mbAutoDispatch = true;

    public GUID getFormationGuid() { return this.mFormationGuid; }
    public void setFormationGuid( GUID formationGuid ) { this.mFormationGuid = formationGuid; }
    public String getStrategyType() { return this.mszStrategyType; }
    public void setStrategyType( String strategyType ) { this.mszStrategyType = strategyType; }
    public long getPageSize() { return this.mnPageSize; }
    public void setPageSize( long pageSize ) { this.mnPageSize = pageSize; }
    public long getFrameSize() { return this.mnFrameSize; }
    public void setFrameSize( long frameSize ) { this.mnFrameSize = frameSize; }
    public long getWindowSize() { return this.mnWindowSize; }
    public void setWindowSize( long windowSize ) { this.mnWindowSize = windowSize; }
    public long getInflightLimit() { return this.mnInflightLimit; }
    public void setInflightLimit( long inflightLimit ) { this.mnInflightLimit = inflightLimit; }
    public boolean isAutoDispatch() { return this.mbAutoDispatch; }
    public void setAutoDispatch( boolean autoDispatch ) { this.mbAutoDispatch = autoDispatch; }
}
