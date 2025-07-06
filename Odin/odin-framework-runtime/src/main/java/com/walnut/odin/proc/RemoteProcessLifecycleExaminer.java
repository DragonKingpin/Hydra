package com.walnut.odin.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageModifier;
import com.walnut.odin.proc.client.RPCRecallSysProcessEventHandler;
import com.walnut.odin.proc.client.SlaveProcessLifecycleIface;

public class RemoteProcessLifecycleExaminer implements ProcessLifecycleExaminer {

    protected Logger                       mLogger;
    protected RemoteProcessManagerNode     mRemoteProcessManagerNode;
    protected SlaveProcessLifecycleIface   mSlaveProcessLifecycleIface;
    protected ProcessManager               mProcessManager;
    protected ImageModifier                mImageModifier;

    public RemoteProcessLifecycleExaminer( RemoteProcessManagerNode remoteProcessManagerNode, SlaveProcessLifecycleIface slaveProcessLifecycleIface ) {
        this.mSlaveProcessLifecycleIface = slaveProcessLifecycleIface;
        this.mRemoteProcessManagerNode   = remoteProcessManagerNode;
        this.mProcessManager             = remoteProcessManagerNode.localProcessManager();
        this.mImageModifier              = this.mProcessManager.getImageModifier();
        this.mLogger                     = LoggerFactory.getLogger( this.getClass() );
    }

    @Override
    public void startProcess( UProcess process ) {
        this.mLogger.info( "[RemoteProcessVitalization] (Process: `{}`, PID: `{}`) <InstructionAccepted>", process.getName(), process.getPID() );
        ExecutionImage image = process.getExecutionImage();
        this.mImageModifier.addSystemProcessEventHandler( image.getEntryPoint(), new RPCRecallSysProcessEventHandler(
                this.mRemoteProcessManagerNode, this.mSlaveProcessLifecycleIface
        ) );

        process.start(); // TODO, Process Joint

        this.mLogger.info( "[RemoteProcessVitalization] (Process: `{}`, PID: `{}`) <InstructionPerformed>", process.getName(), process.getPID() );
    }

}
