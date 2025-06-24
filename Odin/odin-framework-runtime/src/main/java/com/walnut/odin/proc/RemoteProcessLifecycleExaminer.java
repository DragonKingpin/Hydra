package com.walnut.odin.proc;

import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageModifier;
import com.walnut.odin.proc.client.RPCRecallSysProcessEventHandler;
import com.walnut.odin.proc.client.SlaveProcessLifecycleIface;

public class RemoteProcessLifecycleExaminer implements ProcessLifecycleExaminer {

    protected RemoteProcessManagerNode     mRemoteProcessManagerNode;
    protected SlaveProcessLifecycleIface   mSlaveProcessLifecycleIface;
    protected ProcessManager               mProcessManager;
    protected ImageModifier                mImageModifier;

    public RemoteProcessLifecycleExaminer( RemoteProcessManagerNode remoteProcessManagerNode, SlaveProcessLifecycleIface slaveProcessLifecycleIface ) {
        this.mSlaveProcessLifecycleIface = slaveProcessLifecycleIface;
        this.mRemoteProcessManagerNode   = remoteProcessManagerNode;
        this.mProcessManager             = remoteProcessManagerNode.localProcessManager();
        this.mImageModifier              = this.mProcessManager.getImageModifier();
    }

    @Override
    public void startProcess( UProcess process ) {
        ExecutionImage image = process.getExecutionImage();
        this.mImageModifier.addSystemProcessEventHandler( image.getEntryPoint(), new RPCRecallSysProcessEventHandler(
                this.mRemoteProcessManagerNode, this.mSlaveProcessLifecycleIface
        ) );

        process.start(); // TODO, Process Joint
    }

}
