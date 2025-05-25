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

public class ArchUProcess implements UProcess {
    protected Processum              mLocalSystemProc;

    protected GUID                   mProcessID;
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
            @Nullable Processum localSystemProc, GUID guid, String szName,
            UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this.mLocalSystemProc = localSystemProc;
        this.mProcessManager  = processManager;
        this.mProcessID       = guid;
        this.mExecutionImage  = image;
        this.mProcSpace       = procSpace;
        this.mStartupArgs     = startupArgs;
        this.mEnvironmentVars = environmentVars;

        if ( this.mLocalSystemProc == null ) {
            this.mLocalSystemProc = new LocalSystemProcess( szName, parent );
        }
    }

    public ArchUProcess(
            @Nullable Processum localSystemProc, String szName,
            UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this( localSystemProc, processManager.getGuidAllocator().nextGUID(), szName, parent, processManager, image, procSpace, startupArgs, environmentVars );
    }

    public ArchUProcess(
            Processum localSystemProc,
            UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace,
            Map<String, String[]> startupArgs, Map<String, String[]> environmentVars
    ) {
        this( localSystemProc, processManager.getGuidAllocator().nextGUID(), localSystemProc.getName(), parent, processManager, image, procSpace, startupArgs, environmentVars );
    }

    @Override
    public Processum getCurrentLocalSystemProcess() {
        return this.mLocalSystemProc;
    }

    @Override
    public GUID getGuid() {
        return this.mProcessID;
    }

    @Override
    public long getLocalPID() {
        return this.getId();
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

        this.mLastUpdateTime = LocalDateTime.now();
        this.mEndTime        = LocalDateTime.now();
    }


    /** Proxied Processum **/

    @Override
    public Map<Long, Executum> getOwnThreadGroup() {
        return this.mLocalSystemProc.getOwnThreadGroup();
    }

    @Override
    public TaskManager getTaskManager() {
        return this.mLocalSystemProc.getTaskManager();
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mLocalSystemProc.getCreateTime();
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.mLocalSystemProc.getStartTime();
    }

    @Override
    public String getName() {
        return this.mLocalSystemProc.getName();
    }

    @Override
    public void setName( String szName ) {
        this.mLocalSystemProc.setName( szName );
    }

    @Override
    public long getId() {
        return this.mLocalSystemProc.getId();
    }

    @Override
    public RuntimeSystem getSystem() {
        return this.mLocalSystemProc.getSystem();
    }

    @Override
    public Executum parentExecutum() {
        return this.mLocalSystemProc.parentExecutum();
    }

    @Override
    public Executum setThreadAffinity( Thread affinity ) {
        return this.mLocalSystemProc.setThreadAffinity( affinity );
    }

    @Override
    public Thread getAffiliateThread() {
        return this.mLocalSystemProc.getAffiliateThread();
    }

    @Override
    public boolean isTerminated() {
        return this.mLocalSystemProc.isTerminated();
    }

    @Override
    public void start() {
        this.mLocalSystemProc.start();
    }

    @Override
    public void apoptosis() throws ApoptosisRejectSignalException {
        this.mLocalSystemProc.apoptosis();
    }

    @Override
    public void kill() {
        this.mLocalSystemProc.kill();
    }

    @Override
    public void interrupt() {
        this.mLocalSystemProc.interrupt();
    }

    @Override
    public void suspend() {
        this.mLocalSystemProc.suspend();
    }

    @Override
    public void resume() {
        this.mLocalSystemProc.resume();
    }

    @Override
    public void entreatLive() {
        this.mLocalSystemProc.entreatLive();
    }

    @Override
    public Thread.State getState() {
        return this.mLocalSystemProc.getState();
    }

    @Override
    public int getExceptionRestartTime() {
        return this.mLocalSystemProc.getExceptionRestartTime();
    }

    @Override
    public Lifecycle applyExceptionRestartTime( int time ) {
        return this.mLocalSystemProc.applyExceptionRestartTime( time );
    }

    /** Proxied Processum End **/



    static class LocalSystemProcess extends ArchProcessum {
        LocalSystemProcess ( String szName, Processum parent ) {
            super( szName, parent );
        }
    }

}
