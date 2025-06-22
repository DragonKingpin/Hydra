package com.pinecone.hydra.proc;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.ApoptosisRejectSignalException;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Lifecycle;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.executum.TaskManager;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.entity.ElementNode;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public abstract class ArchUProcess implements UProcess {
    protected Processum              mLocalProcess;

    protected GUID                   mProcessID;
    protected GUID                   mParentPID;
    protected ObjectTable            mObjectTable;
    protected ProcSpace              mProcSpace;

    protected ProcessManager         mProcessManager;

    protected ExecutionImage         mExecutionImage;

    protected Map<String, String[]>  mStartupArgs;
    protected Map<String, String[]>  mEnvironmentVars;

    protected ControllableLevel      mControllableLevel;
    protected LocalDateTime          mEndTime;
    protected LocalDateTime          mLastUpdateTime;

    public ArchUProcess(
            @Nullable Processum localProcess, GUID guid, String szName,
            @Nullable UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this.mLocalProcess      = localProcess;
        this.mProcessManager    = processManager;
        this.mProcessID         = guid;
        this.mExecutionImage    = image;
        this.mProcSpace         = procSpace;
        this.mStartupArgs       = startupArgs;
        this.mEnvironmentVars   = environmentVars;
        this.mControllableLevel = image.getControllableLevel();

        if ( this.mLocalProcess == null ) {
            this.mLocalProcess = new LocalSystemProcess( szName, parent );
        }

        if ( parent != null ) {
            this.mParentPID     = parent.getPID();
        }
    }

    public ArchUProcess(
            @Nullable Processum localSystemProc, String szName,
            @Nullable UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this( localSystemProc, processManager.getGuidAllocator().nextGUID(), szName, parent, processManager, image, procSpace, startupArgs, environmentVars );
    }

    public ArchUProcess(
            Processum localSystemProc,
            @Nullable UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this( localSystemProc, processManager.getGuidAllocator().nextGUID(), localSystemProc.getName(), parent, processManager, image, procSpace, startupArgs, environmentVars );
    }

    @Override
    public Processum affinityLocalProcess() {
        return this.mLocalProcess;
    }

    @Override
    public GUID getGuid() {
        return this.mProcessID;
    }

    @Override
    public long getLocalPID() {
        return this.getExecutumId();
    }

    @Override
    public ElementNode getAccount() {
        return null;
    }

    @Override
    public UProcess parentProcess() {
        return (UProcess) this.parentExecutum();
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
    public ProcessManager getOwnedProcessManager() {
        return this.mProcessManager;
    }

    @Override
    public GUID getParentProcessId() {
        if ( this.parentProcess() != null ) {
            return this.parentProcess().getGuid();
        }

        return null;
    }

    @Override
    public long getParentLocalPID() {
        if ( this.parentProcess() != null ) {
            return this.parentProcess().getLocalPID();
        }
        return -1;
    }

    @Override
    public ProcSpace getProcNamespace() {
        return this.mProcSpace;
    }

    @Override
    public Map<String, String[]> getStartupArguments() {
        return this.mStartupArgs;
    }

    @Override
    public Map<String, String[]> getEnvironmentVariables() {
        return this.mEnvironmentVars;
    }

    @Override
    public ObjectTable getObjectTable() {
        return this.mObjectTable;
    }

    @Override
    public ExecutionImage getExecutionImage() {
        return this.mExecutionImage;
    }

    @Override
    public ControllableLevel getControllableLevel() {
        return this.mControllableLevel;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.mEndTime;
    }

    @Override
    public LocalDateTime getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    @Override
    public void triggerUpdateTerminationStatus() {
        if ( this.getState() != Thread.State.TERMINATED ) {
            throw new IllegalStateException( "Bad time to trigger, I am still alive!" );
        }

        this.triggerAfterRunnableTerminationStatus();
    }

    @Override
    public void triggerAfterRunnableTerminationStatus() {
        this.mLastUpdateTime = LocalDateTime.now();
        this.mEndTime        = LocalDateTime.now();
    }

    /** Proxied Processum **/

    @Override
    public Map<Long, Executum> getOwnThreadGroup() {
        return this.mLocalProcess.getOwnThreadGroup();
    }

    @Override
    public TaskManager getTaskManager() {
        return this.mLocalProcess.getTaskManager();
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mLocalProcess.getCreateTime();
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.mLocalProcess.getStartTime();
    }

    @Override
    public String getName() {
        return this.mLocalProcess.getName();
    }

    @Override
    public void setName( String szName ) {
        this.mLocalProcess.setName( szName );
    }

    @Override
    public long getExecutumId() {
        return this.mLocalProcess.getExecutumId();
    }

    @Override
    public RuntimeSystem parentSystem() {
        return this.mLocalProcess.parentSystem();
    }

    @Override
    public RuntimeSystem revealNearestSystem() {
        return this.mLocalProcess.revealNearestSystem();
    }

    @Override
    public Executum parentExecutum() {
        return this.mLocalProcess.parentExecutum();
    }

    @Override
    public Executum setThreadAffinity( Thread affinity ) {
        return this.mLocalProcess.setThreadAffinity( affinity );
    }

    @Override
    public Thread getAffiliateThread() {
        return this.mLocalProcess.getAffiliateThread();
    }

    @Override
    public boolean isTerminated() {
        return this.mLocalProcess.isTerminated();
    }

    @Override
    public void start() {
        this.mLocalProcess.start();
    }

    @Override
    public void apoptosis() throws ApoptosisRejectSignalException {
        this.mLocalProcess.apoptosis();
    }

    @Override
    public void kill() {
        this.mLocalProcess.kill();
    }

    @Override
    public void interrupt() {
        this.mLocalProcess.interrupt();
    }

    @Override
    public void suspend() {
        this.mLocalProcess.suspend();
    }

    @Override
    public void resume() {
        this.mLocalProcess.resume();
    }

    @Override
    public void entreatLive() {
        this.mLocalProcess.entreatLive();
    }

    @Override
    public Thread.State getState() {
        return this.mLocalProcess.getState();
    }

    @Override
    public int getExceptionRestartTime() {
        return this.mLocalProcess.getExceptionRestartTime();
    }

    @Override
    public Lifecycle applyExceptionRestartTime( int time ) {
        return this.mLocalProcess.applyExceptionRestartTime( time );
    }

    /** Proxied Processum End **/



    static class LocalSystemProcess extends ArchProcessum {
        LocalSystemProcess ( String szName, Processum parent ) {
            super( szName, parent );
        }
    }

}
