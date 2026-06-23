package com.walnut.odin.formation.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FormationGroupUpsertRequest implements Pinenut {
    protected GUID    mGuid;
    protected String  mszIdentifier;
    protected String  mszTitle;
    protected GUID    mProjectGuid;
    protected String  mszStrategyType;
    protected long    mnPageSize;
    protected long    mnFrameSize;
    protected long    mnWindowSize;
    protected long    mnInflightLimit;
    protected boolean mbEnable;
    protected String  mszDescription;

    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public String getIdentifier() { return this.mszIdentifier; }
    public void setIdentifier( String identifier ) { this.mszIdentifier = identifier; }
    public String getTitle() { return this.mszTitle; }
    public void setTitle( String title ) { this.mszTitle = title; }
    public GUID getProjectGuid() { return this.mProjectGuid; }
    public void setProjectGuid( GUID projectGuid ) { this.mProjectGuid = projectGuid; }
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
    public boolean isEnable() { return this.mbEnable; }
    public void setEnable( boolean enable ) { this.mbEnable = enable; }
    public String getDescription() { return this.mszDescription; }
    public void setDescription( String description ) { this.mszDescription = description; }
}
