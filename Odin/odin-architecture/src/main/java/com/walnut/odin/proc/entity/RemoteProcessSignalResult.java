package com.walnut.odin.proc.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class RemoteProcessSignalResult implements Pinenut {
    protected boolean accepted;

    protected boolean schedulerClosed;

    protected boolean physicalClosed;

    protected boolean operatorActionRequired;

    protected String processId;

    protected String signal;

    protected String reason;

    protected String transport;

    protected String terminator;

    protected String message;

    public boolean isAccepted() {
        return this.accepted;
    }

    public void setAccepted( boolean accepted ) {
        this.accepted = accepted;
    }

    public boolean isSchedulerClosed() {
        return this.schedulerClosed;
    }

    public void setSchedulerClosed( boolean schedulerClosed ) {
        this.schedulerClosed = schedulerClosed;
    }

    public boolean isPhysicalClosed() {
        return this.physicalClosed;
    }

    public void setPhysicalClosed( boolean physicalClosed ) {
        this.physicalClosed = physicalClosed;
    }

    public boolean isOperatorActionRequired() {
        return this.operatorActionRequired;
    }

    public void setOperatorActionRequired( boolean operatorActionRequired ) {
        this.operatorActionRequired = operatorActionRequired;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId( String processId ) {
        this.processId = processId;
    }

    public String getSignal() {
        return this.signal;
    }

    public void setSignal( String signal ) {
        this.signal = signal;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason( String reason ) {
        this.reason = reason;
    }

    public String getTransport() {
        return this.transport;
    }

    public void setTransport( String transport ) {
        this.transport = transport;
    }

    public String getTerminator() {
        return this.terminator;
    }

    public void setTerminator( String terminator ) {
        this.terminator = terminator;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }
}
