package com.walnut.odin.proc.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.RemoteVitalizationStatus;

public class RemoteVitalizationResponse implements Pinenut {

    protected long mnLocalPID;

    protected GUID mPID;

    protected String mszPID;

    protected int mnStatus;

    protected String mszErrorMsg;

    public RemoteVitalizationResponse() {
        this.mnStatus = RemoteVitalizationStatus.Vitalized.getCode();
    }

    public long getLocalPID() {
        return this.mnLocalPID;
    }

    public void setLocalPID( long nLocalPID ) {
        this.mnLocalPID = nLocalPID;
    }

    public String getPID() {
        return this.mszPID;
    }

    public void setPID( String szPID ) {
        this.mszPID = szPID;
    }

    public void setStatus( int nStatus ) {
        this.mnStatus = nStatus;
    }

    public int getStatus() {
        return this.mnStatus;
    }

    public String getErrorMsg() {
        return this.mszErrorMsg;
    }

    public void setMszErrorMsg( String szErrorMsg ) {
        this.mszErrorMsg = szErrorMsg;
    }

    public void setProcessID( GUID pid ) {
        this.setPID( pid.toString() );
        this.mPID = pid;
    }

    public GUID optProcessID() {
        return this.mPID;
    }

    public void setRemoteVitalizationStatus( RemoteVitalizationStatus status ) {
        this.setStatus( status.getCode() );
    }

    public RemoteVitalizationStatus optStatus() {
        return RemoteVitalizationStatus.getByCode( this.getStatus() );
    }

}
