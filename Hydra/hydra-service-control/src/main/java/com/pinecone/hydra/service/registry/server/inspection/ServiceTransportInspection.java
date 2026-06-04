package com.pinecone.hydra.service.registry.server.inspection;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceTransportInspection implements Pinenut {

    protected String  mszType;

    protected boolean mbStarted;

    protected boolean mbTerminated;

    protected boolean mbAvailable;

    protected boolean mbRuntimeIfaceCompileSupported;

    protected int     mnConnectedClientCount;

    protected int     mnControllerCount;

    protected int     mnIfaceCount;

    protected String  mszControllerSummary;

    protected String  mszIfaceSummary;

    protected String  mszLastError;

    public String getType() {
        return this.mszType;
    }

    public void setType( String szType ) {
        this.mszType = szType;
    }

    public boolean isStarted() {
        return this.mbStarted;
    }

    public void setStarted( boolean bStarted ) {
        this.mbStarted = bStarted;
    }

    public boolean isTerminated() {
        return this.mbTerminated;
    }

    public void setTerminated( boolean bTerminated ) {
        this.mbTerminated = bTerminated;
    }

    public boolean isAvailable() {
        return this.mbAvailable;
    }

    public void setAvailable( boolean bAvailable ) {
        this.mbAvailable = bAvailable;
    }

    public boolean isRuntimeIfaceCompileSupported() {
        return this.mbRuntimeIfaceCompileSupported;
    }

    public void setRuntimeIfaceCompileSupported( boolean bRuntimeIfaceCompileSupported ) {
        this.mbRuntimeIfaceCompileSupported = bRuntimeIfaceCompileSupported;
    }

    public int getConnectedClientCount() {
        return this.mnConnectedClientCount;
    }

    public void setConnectedClientCount( int nConnectedClientCount ) {
        this.mnConnectedClientCount = nConnectedClientCount;
    }

    public int getControllerCount() {
        return this.mnControllerCount;
    }

    public void setControllerCount( int nControllerCount ) {
        this.mnControllerCount = nControllerCount;
    }

    public int getIfaceCount() {
        return this.mnIfaceCount;
    }

    public void setIfaceCount( int nIfaceCount ) {
        this.mnIfaceCount = nIfaceCount;
    }

    public String getControllerSummary() {
        return this.mszControllerSummary;
    }

    public void setControllerSummary( String szControllerSummary ) {
        this.mszControllerSummary = szControllerSummary;
    }

    public String getIfaceSummary() {
        return this.mszIfaceSummary;
    }

    public void setIfaceSummary( String szIfaceSummary ) {
        this.mszIfaceSummary = szIfaceSummary;
    }

    public String getLastError() {
        return this.mszLastError;
    }

    public void setLastError( String szLastError ) {
        this.mszLastError = szLastError;
    }
}
