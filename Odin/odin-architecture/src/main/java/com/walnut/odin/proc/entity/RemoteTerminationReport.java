package com.walnut.odin.proc.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.RemoteTerminationStatus;

public class RemoteTerminationReport implements Pinenut {

    protected long mnLocalPID;

    protected GUID mPID;

    protected String mszPID;

    protected int mnTerminationStatus;

    protected int mnExitCode;

    protected String mszErrorMsg;

    public RemoteTerminationReport() {
        this.mnTerminationStatus = RemoteTerminationStatus.Expected.getCode();
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

    public void setTerminationStatus( int nStatus ) {
        this.mnTerminationStatus = nStatus;
    }

    public int getTerminationStatus() {
        return this.mnTerminationStatus;
    }

    public String getErrorMsg() {
        return this.mszErrorMsg;
    }

    public void setErrorMsg( String szErrorMsg ) {
        this.mszErrorMsg = szErrorMsg;
    }

    public void setProcessID( GUID pid ) {
        this.setPID( pid.toString() );
        this.mPID = pid;
    }

    public GUID optProcessID() {
        return this.mPID;
    }

    public int getExitCode() {
        return this.mnExitCode;
    }

    public void setExitCode( int nExitCode ) {
        this.mnExitCode = nExitCode;
    }

    public void setRemoteTerminationStatus( RemoteTerminationStatus status ) {
        this.setTerminationStatus( status.getCode() );
    }

    public RemoteTerminationStatus optStatus() {
        return RemoteTerminationStatus.getByCode( this.getTerminationStatus() );
    }

}

