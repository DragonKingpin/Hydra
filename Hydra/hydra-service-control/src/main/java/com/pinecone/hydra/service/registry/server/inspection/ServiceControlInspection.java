package com.pinecone.hydra.service.registry.server.inspection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceControlInspection implements Pinenut {

    protected String                       mszOverallStatus;

    protected boolean                      mbServiceInstrumentReady;

    protected boolean                      mbServiceManagerStarted;

    protected int                          mnRegisteredServiceCount;

    protected int                          mnRuntimeInstanceCount;

    protected int                          mnConnectedClientCount;

    protected List<ServiceTransportInspection> mTransports = new ArrayList<>();

    protected Map<String, Integer>         mInstanceStatusCounts = new LinkedHashMap<>();

    protected String                       mszDiagnosticMessage;

    public String getOverallStatus() {
        return this.mszOverallStatus;
    }

    public void setOverallStatus( String szOverallStatus ) {
        this.mszOverallStatus = szOverallStatus;
    }

    public boolean isServiceInstrumentReady() {
        return this.mbServiceInstrumentReady;
    }

    public void setServiceInstrumentReady( boolean bServiceInstrumentReady ) {
        this.mbServiceInstrumentReady = bServiceInstrumentReady;
    }

    public boolean isServiceManagerStarted() {
        return this.mbServiceManagerStarted;
    }

    public void setServiceManagerStarted( boolean bServiceManagerStarted ) {
        this.mbServiceManagerStarted = bServiceManagerStarted;
    }

    public int getRegisteredServiceCount() {
        return this.mnRegisteredServiceCount;
    }

    public void setRegisteredServiceCount( int nRegisteredServiceCount ) {
        this.mnRegisteredServiceCount = nRegisteredServiceCount;
    }

    public int getRuntimeInstanceCount() {
        return this.mnRuntimeInstanceCount;
    }

    public void setRuntimeInstanceCount( int nRuntimeInstanceCount ) {
        this.mnRuntimeInstanceCount = nRuntimeInstanceCount;
    }

    public int getConnectedClientCount() {
        return this.mnConnectedClientCount;
    }

    public void setConnectedClientCount( int nConnectedClientCount ) {
        this.mnConnectedClientCount = nConnectedClientCount;
    }

    public List<ServiceTransportInspection> getTransports() {
        return this.mTransports;
    }

    public void setTransports( List<ServiceTransportInspection> transports ) {
        if ( transports == null ) {
            this.mTransports = new ArrayList<>();
            return;
        }
        this.mTransports = transports;
    }

    public Map<String, Integer> getInstanceStatusCounts() {
        return this.mInstanceStatusCounts;
    }

    public void setInstanceStatusCounts( Map<String, Integer> instanceStatusCounts ) {
        if ( instanceStatusCounts == null ) {
            this.mInstanceStatusCounts = new LinkedHashMap<>();
            return;
        }
        this.mInstanceStatusCounts = instanceStatusCounts;
    }

    public String getDiagnosticMessage() {
        return this.mszDiagnosticMessage;
    }

    public void setDiagnosticMessage( String szDiagnosticMessage ) {
        this.mszDiagnosticMessage = szDiagnosticMessage;
    }
}
