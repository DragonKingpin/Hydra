package com.walnut.odin.proc.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class UProcessRuntimeMeta implements Pinenut {
    private String      mszName;

    private long        mnLocalPID;

    private String      mszParentPID;

    private String      mszProcessId;

    private String      mszCreateTime;
    private String      mszStartTime;
    private String      mszEndTime;
    private String      mszLastUpdateTime;
    private String      mszStatus;
    private boolean     mbTerminated;


    public UProcessRuntimeMeta() {

    }

    public String getCreateTime() {
        return this.mszCreateTime;
    }

    public void setCreateTime( String createTime ) {
        this.mszCreateTime = createTime;
    }

    public String getStartTime() {
        return this.mszStartTime;
    }

    public void setStartTime( String startTime ) {
        this.mszStartTime = startTime;
    }

    public String getEndTime() {
        return this.mszEndTime;
    }

    public void setEndTime( String endTime ) {
        this.mszEndTime = endTime;
    }

    public String getLastUpdateTime() {
        return this.mszLastUpdateTime;
    }

    public void setLastUpdateTime( String lastUpdateTime ) {
        this.mszLastUpdateTime = lastUpdateTime;
    }



    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String status ) {
        this.mszStatus = status;
    }

    public boolean isTerminated() {
        return this.mbTerminated;
    }

    public void setTerminated( boolean terminated ) {
        this.mbTerminated = terminated;
    }




    public String getName() {
        return this.mszName;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    public String getParentPID() {
        return this.mszParentPID;
    }

    public void setParentPID( String szParentPID ) {
        this.mszParentPID = szParentPID;
    }

    public long getLocalPID() {
        return mnLocalPID;
    }

    public void setLocalPID( long pid ) {
        this.mnLocalPID = pid;
    }

    public String getPID() {
        return mszProcessId;
    }

    public void setPID( String pid ) {
        this.mszProcessId = pid;
    }
}
