package com.walnut.odin.task.troll;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEventHandler;

public class LaunchFeature implements Pinenut {

    private boolean retry;

    private URI designatedImageURI;

    private UProcess parentProcess;

    private String processorDesignated;

    private GUID parentPid;

    private Map<String, String> startupArgs;

    private Map<String, String> contextEnvironmentVars;

    private LocalDateTime bizTimeEpoch;

    private List<ProcessEventHandler> sysProcEventHandlers;

    private boolean allowAsymmetricImage = true;

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

    public Map<String, String> getStartupArgs() {
        return this.startupArgs;
    }

    public Map<String, String> getContextEnvironmentVars() {
        return this.contextEnvironmentVars;
    }

    public GUID getParentPid() {
        return this.parentPid;
    }

    public List<ProcessEventHandler> getSysProcEventHandlers() {
        return this.sysProcEventHandlers;
    }

    public boolean isAllowAsymmetricImage() {
        return this.allowAsymmetricImage;
    }

    public void setAllowAsymmetricImage( boolean allowAsymmetricImage ) {
        this.allowAsymmetricImage = allowAsymmetricImage;
    }

    public LaunchFeature withAllowAsymmetricImage( boolean allowAsymmetricImage ) {
        this.allowAsymmetricImage = allowAsymmetricImage;
        return this;
    }

    public LaunchFeature mergeLaunchOptions( LaunchFeature that ) {
        if ( that == null ) {
            return this;
        }

        this.allowAsymmetricImage = that.isAllowAsymmetricImage();

        if ( that.getProcessorDesignated() != null ) {
            this.processorDesignated = that.getProcessorDesignated();
        }

        if ( that.getDesignatedImageURI() != null ) {
            this.designatedImageURI = that.getDesignatedImageURI();
        }

        if ( that.getStartupArgs() != null ) {
            this.startupArgs = that.getStartupArgs();
        }

        if ( that.getContextEnvironmentVars() != null ) {
            this.contextEnvironmentVars = that.getContextEnvironmentVars();
        }

        if ( that.getParentPid() != null ) {
            this.parentPid = that.getParentPid();
        }

        if ( that.getParentProcess() != null ) {
            this.parentProcess = that.getParentProcess();
        }

        if ( that.getBizTimeEpoch() != null ) {
            this.bizTimeEpoch = that.getBizTimeEpoch();
        }

        return this;
    }

    public String getProcessorDesignated() {
        return this.processorDesignated;
    }

    public LaunchFeature withProcessorDesignated( String processorName ) {
        this.processorDesignated = processorName;
        return this;
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

    public LaunchFeature withStartupArgs( Map<String, String> startupArgs ) {
        this.startupArgs = startupArgs;
        return this;
    }

    public LaunchFeature withContextEnvironmentVars( Map<String, String> contextEnvironmentVars ) {
        this.contextEnvironmentVars = contextEnvironmentVars;
        return this;
    }

    public LaunchFeature withSysProcEventHandlers( List<ProcessEventHandler> sysProcEventHandlers ) {
        this.sysProcEventHandlers = sysProcEventHandlers;
        return this;
    }

    public LaunchFeature withSysProcEventHandlers( ProcessEventHandler handler ) {
        if ( this.sysProcEventHandlers == null ) {
            this.sysProcEventHandlers = new ArrayList<>();
        }
        this.sysProcEventHandlers.add( handler );
        return this;
    }

}
