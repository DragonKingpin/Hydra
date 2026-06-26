package com.walnut.odin.formation.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FormationFrameQuery implements Pinenut {
    protected GUID   runGuid;
    protected String frameStatus;
    protected Long   pageNo;
    protected Long   frameNo;
    protected String taskKeyword;
    protected String instanceGuid;
    protected String queueType;

    public GUID getRunGuid() { return this.runGuid; }
    public void setRunGuid( GUID runGuid ) { this.runGuid = runGuid; }

    public String getFrameStatus() { return this.frameStatus; }
    public void setFrameStatus( String frameStatus ) { this.frameStatus = frameStatus; }

    public Long getPageNo() { return this.pageNo; }
    public void setPageNo( Long pageNo ) { this.pageNo = pageNo; }

    public Long getFrameNo() { return this.frameNo; }
    public void setFrameNo( Long frameNo ) { this.frameNo = frameNo; }

    public String getTaskKeyword() { return this.taskKeyword; }
    public void setTaskKeyword( String taskKeyword ) { this.taskKeyword = taskKeyword; }

    public String getInstanceGuid() { return this.instanceGuid; }
    public void setInstanceGuid( String instanceGuid ) { this.instanceGuid = instanceGuid; }

    public String getQueueType() { return this.queueType; }
    public void setQueueType( String queueType ) { this.queueType = queueType; }
}
