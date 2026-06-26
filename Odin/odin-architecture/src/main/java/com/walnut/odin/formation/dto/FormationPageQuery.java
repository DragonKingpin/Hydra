package com.walnut.odin.formation.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FormationPageQuery implements Pinenut {
    protected GUID   runGuid;
    protected Long   pageNo;
    protected String pageStatus;

    public GUID getRunGuid() {
        return this.runGuid;
    }

    public void setRunGuid( GUID runGuid ) {
        this.runGuid = runGuid;
    }

    public Long getPageNo() {
        return this.pageNo;
    }

    public void setPageNo( Long pageNo ) {
        this.pageNo = pageNo;
    }

    public String getPageStatus() {
        return this.pageStatus;
    }

    public void setPageStatus( String pageStatus ) {
        this.pageStatus = pageStatus;
    }
}
