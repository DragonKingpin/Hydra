package com.walnut.odin.task.troll;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;

public class LaunchFeature implements Pinenut {

    private boolean retry;

    private URI designatedImageURI;

    private UProcess parentProcess;

    private GUID parentPid;

    private Map<String, String[]> startupArgs;

    private Map<String, String[]> contextEnvironmentVars;

    private LocalDateTime bizTimeEpoch;

    public LaunchFeature() {
        this.bizTimeEpoch = LocalDateTime.now().minusDays( 1 ); // dtm
    }

    public boolean isRetry() {
        return this.retry;
    }

    public URI getDesignatedImageURI() {
        return this.designatedImageURI;
    }

    public UProcess getParentProcess() {
        return this.parentProcess;
    }

    public Map<String, String[]> getStartupArgs() {
        return this.startupArgs;
    }

    public Map<String, String[]> getContextEnvironmentVars() {
        return this.contextEnvironmentVars;
    }

    public GUID getParentPid() {
        return this.parentPid;
    }

    public LaunchFeature withParentPid( GUID pid ) {
        this.parentPid = pid;
        return this;
    }

    public LocalDateTime getBizTimeEpoch() {
        return this.bizTimeEpoch;
    }

    public void setBizTimeEpoch( LocalDateTime bizTimeEpoch ) {
        this.bizTimeEpoch = bizTimeEpoch;
    }

    public LaunchFeature withRetry(boolean retry ) {
        this.retry = retry;
        return this;
    }

    public LaunchFeature withDesignatedImageURI( URI designatedImageURI ) {
        this.designatedImageURI = designatedImageURI;
        return this;
    }

    public LaunchFeature withParentProcess( UProcess parent ) {
        this.parentProcess = parent;
        this.parentPid = parent.getPID();
        return this;
    }

    public LaunchFeature withStartupArgs( Map<String, String[]> startupArgs ) {
        this.startupArgs = startupArgs;
        return this;
    }

    public LaunchFeature withContextEnvironmentVars( Map<String, String[]> contextEnvironmentVars ) {
        this.contextEnvironmentVars = contextEnvironmentVars;
        return this;
    }

}