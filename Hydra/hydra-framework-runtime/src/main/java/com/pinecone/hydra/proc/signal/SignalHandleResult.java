package com.pinecone.hydra.proc.signal;

import com.pinecone.framework.system.prototype.Pinenut;

public class SignalHandleResult implements Pinenut {
    protected boolean accepted;
    protected boolean schedulerClosed;
    protected boolean physicalClosed;
    protected boolean operatorActionRequired;
    protected String terminator;
    protected String message;

    public static SignalHandleResult accepted() {
        SignalHandleResult result = new SignalHandleResult();
        result.setAccepted( true );
        return result;
    }

    public static SignalHandleResult rejected( String message ) {
        SignalHandleResult result = new SignalHandleResult();
        result.setAccepted( false );
        result.setMessage( message );
        return result;
    }

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
