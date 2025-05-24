package com.pinecone.hydra.proc;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.ns.ProcSpace;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public abstract class ArchUProcess extends ArchProcessum implements UProcess {

    protected GUID                 mProcessID;
    protected ObjectTable          mObjectTable;
    protected ProcSpace            mProcSpace;

    protected ProcessManager       mProcessManager;

    protected ExecutionImage       mExecutionImage;

    protected Map<String, String>  mStartupArgs;
    protected Map<String, String>  mEnvironmentVars;

    protected ControllableLevel    mControllableLevel;
    protected LocalDateTime        mEndTime;
    protected LocalDateTime        mLastUpdateTime;

    public ArchUProcess( GUID guid, String szName, UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace, Map<String, String> startupArgs, Map<String, String> environmentVars ) {
        super( szName, parent );

        this.mProcessManager  = processManager;
        this.mProcessID       = guid;
        this.mExecutionImage  = image;
        this.mProcSpace       = procSpace;
        this.mStartupArgs     = startupArgs;
        this.mEnvironmentVars = environmentVars;
    }

    public ArchUProcess( String szName, UProcess parent, ProcessManager processManager, ExecutionImage image, ProcSpace procSpace, Map<String, String> startupArgs, Map<String, String> environmentVars ) {
        this( processManager.getGuidAllocator().nextGUID(), szName, parent, processManager, image, procSpace, startupArgs, environmentVars );
    }

    @Override
    public GUID getGuid() {
        return this.mProcessID;
    }

    @Override
    public UProcess parentProcess() {
        return (UProcess) this.parentExecutum();
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
    public Map<String, String> getStartupArguments() {
        return this.mStartupArgs;
    }

    @Override
    public Map<String, String> getEnvironmentVariables() {
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

}
