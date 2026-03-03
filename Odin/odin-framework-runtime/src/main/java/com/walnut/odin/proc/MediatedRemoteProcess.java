package com.walnut.odin.proc;

import com.pinecone.framework.system.ApoptosisRejectSignalException;
import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Lifecycle;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.executum.TaskManager;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.ProcessActionTape;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.entity.ElementNode;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;
import com.pinecone.hydra.proc.tomb.RuntimeTombstone;
import com.pinecone.hydra.system.ko.entity.ObjectTable;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MediatedRemoteProcess implements RemoteProcess {

    protected RemoteProcessManagerServer          mRemoteProcessManagerServer;

    protected ProcessManager                      mProcessManager;

    protected ExecutionImage                      mExecutionImage;

    protected String                              mszName;

    protected long                                mnControlClientId;

    protected long                                mnLocalPID;

    protected GUID                                mParentPID;

    protected GUID                                mProcessId;

    protected Map<String, String[]>               mStartupArguments;

    protected Map<String, String[]>               mEnvironmentVariables;

    protected List<ProcessRemoteEventHandler>     mRemoteEventHandlers;

    public MediatedRemoteProcess(
            long controlClientId, RemoteProcessManagerServer server, String name, long localPID, GUID processId,
            Map<String, String[]> startupArguments, Map<String, String[]> environmentVariables
    ) {
        this.mnControlClientId           = controlClientId;
        this.mRemoteProcessManagerServer = server;
        this.mszName                     = name;
        this.mnLocalPID                  = localPID;
        this.mProcessId                  = processId;
        this.mStartupArguments           = startupArguments;
        this.mEnvironmentVariables       = environmentVariables;
        this.mRemoteEventHandlers        = new ArrayList<>();
    }

    public MediatedRemoteProcess( long controlClientId, RemoteProcessManagerServer server, String name, long pid, GUID guid ) {
        this( controlClientId, server, name, pid, guid, null, null );
    }

    @Override
    public void addRemoteEventHandler( ProcessRemoteEventHandler handler ) {
        this.mRemoteEventHandlers.add( handler );
    }

    @Override
    public void removeRemoteEventHandler( ProcessRemoteEventHandler handler ) {
        this.mRemoteEventHandlers.remove( handler );
    }

    @Override
    public int remoteEventHandlerSize() {
        return this.mRemoteEventHandlers.size();
    }

    @Override
    public void notifyRemoteEvent( long pmClientId, ProcessEvent event, Object caused ) {
        for ( ProcessRemoteEventHandler handler : this.mRemoteEventHandlers ) {
            handler.fired( pmClientId, event, caused );
        }
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    public long getControlClientId() {
        return this.mnControlClientId;
    }

    @Override
    public long getLocalPID() {
        return this.mnLocalPID;
    }

    @Override
    public GUID actualParentPID() {
        return this.mParentPID;
    }

    @Override
    public void applyActualParentPID( GUID pid ) {
        this.mParentPID = pid;
    }

    @Override
    public void setName( String szName ) {
        this.mszName = szName;
    }

    @Override
    public long getExecutumId() {
        return this.mnLocalPID;
    }

    @Override
    public UProcessRuntimeMeta retrieveRemoteRuntimeMeta() throws RemoteProcessLifecycleException {
        return this.mRemoteProcessManagerServer.queryProcessRuntimeMeta( this.mProcessId );
    }

    protected UProcessRuntimeMeta optRemoteRuntimeMeta() throws IllegalStateException {
        try {
            return this.mRemoteProcessManagerServer.queryProcessRuntimeMeta( this.mProcessId );
        }
        catch ( RemoteProcessLifecycleException e ) {
            throw new IllegalStateException( e );
        }
    }

    @Override
    public RuntimeSystem parentSystem() {
        return null;
    }

    @Override
    public RuntimeSystem revealNearestSystem() {
        return null;
    }

    @Override
    public Executum parentExecutum() {
        return null;
    }

    @Override
    public Executum setThreadAffinity( Thread affinity ) {
        throw new NotImplementedException( "`RemoteProcess` has no thread affinity, so it cannot be set." );
    }

    @Override
    public Thread getAffiliateThread() {
        return null;
    }

    @Override
    public boolean isTerminated() {
        UProcessRuntimeMeta meta = this.optRemoteRuntimeMeta();
        return meta.isTerminated();
    }

    @Override
    public GUID getGuid() {
        return this.mProcessId;
    }

    @Override
    public GUID getParentProcessId() {
        return this.mParentPID;
    }

    @Override
    public long getParentLocalPID() {
        return 0;
    }

    @Override
    public LocalDateTime remoteGetEndTime() {
        return null;
    }

    @Override
    public LocalDateTime remoteGetLastUpdateTime() {
        return null;
    }

    @Override
    public UProcess parentProcess() {
        return null;
    }

    @Override
    public ProcessManager getOwnedProcessManager() {
        return this.mProcessManager;
    }

    @Override
    public ProcSpace getProcNamespace() {
        return null;
    }

    @Override
    public RuntimeTombstone getRuntimeTombstone() {
        return null;
    }

    @Override
    public ObjectTable getObjectTable() {
        return null;
    }

    @Override
    public ExecutionImage getExecutionImage() {
        return this.mExecutionImage;
    }

    @Override
    public ControllableLevel getControllableLevel() {
        return null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return null;
    }

    @Override
    public LocalDateTime getLastUpdateTime() {
        return null;
    }

    @Override
    public Map<String, String[]> getStartupArguments() {
        return this.mStartupArguments;
    }

    @Override
    public Map<String, String[]> getEnvironmentVariables() {
        return this.mEnvironmentVariables;
    }


    @Override
    public Processum affinityLocalProcess() {
        return null;
    }

    @Override
    public void triggerUpdateTerminationStatus() {

    }

    @Override
    public void triggerAfterRunnableTerminationStatus() {

    }

    @Override
    public void start() throws ProvokeHandleException {
        try {
            this.mRemoteProcessManagerServer.startRemoteUProcess( this.mProcessId );
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new ProvokeHandleException( e );
        }
    }

    @Override
    public Map<Long, Executum> getOwnThreadGroup() {
        return null;
    }

    @Override
    public TaskManager getTaskManager() {
        return null;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return null;
    }

    @Override
    public LocalDateTime getStartTime() {
        return null;
    }

    @Override
    public void apoptosis() throws ApoptosisRejectSignalException {

    }

    @Override
    public void kill() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void suspend() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void entreatLive() {

    }

    @Override
    public Thread.State getState() {
        return null;
    }

    @Override
    public ElementNode getAccount() {
        return null;
    }

    @Override
    public int getExceptionRestartTime() {
        return 0;
    }

    @Override
    public Lifecycle applyExceptionRestartTime( int time ) {
        return null;
    }

    @Override
    public ProcessActionTape actionTape() {
        return null;
    }
}
