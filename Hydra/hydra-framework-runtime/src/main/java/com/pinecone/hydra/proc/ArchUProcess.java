package com.pinecone.hydra.proc;

import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.entity.ObjectTable;

public abstract class ArchUProcess extends ArchProcessum implements UProcess {

    protected GUID mProcessID;

    protected ObjectTable mObjectTable;

    public ArchUProcess( String szName, UProcess parent ) {
        super( szName, parent );
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

}
