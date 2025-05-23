package com.pinecone.hydra.proc;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public abstract class ArchUProcess extends ArchProcessum implements UProcess {

    protected GUID                 mProcessID;

    protected ObjectTable          mObjectTable;

    protected ProcessManager       mProcessManager;

    protected ExecutionImage       mExecutionImage;

    protected ControllableLevel    mControllableLevel;

    protected Map<String, String>  mStartupArgs;

    protected Map<String, String>  mEnvironmentVars;


    protected LocalDateTime        mEndTime;
    protected LocalDateTime        mLastUpdateTime;

    public ArchUProcess( GUID guid, String szName, UProcess parent, ProcessManager processManager ) {
        super( szName, parent );

        this.mProcessManager = processManager;
        this.mProcessID      = guid;
    }

    public ArchUProcess( String szName, UProcess parent, ProcessManager processManager ) {
        this( processManager.getGuidAllocator().nextGUID(), szName, parent, processManager );
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
