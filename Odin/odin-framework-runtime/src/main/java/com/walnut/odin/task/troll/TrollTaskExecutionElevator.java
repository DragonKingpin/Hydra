package com.walnut.odin.task.troll;

import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.RavenTaskInstance;

public class TrollTaskExecutionElevator implements TaskExecutionElevator {

    protected RemoteProcessManagerServer mRemoteProcessManagerServer;

    protected ProcessManager mProcessManager;

    @Override
    public ProcessManager processManager() {
        return this.mProcessManager;
    }

    @Override
    public UProcess launch( RavenTaskInstance instance ) {
        UProcess process = instance.affinityProcess();
        if ( process == null ) {
            instance.startLocalProcess();
            process = instance.affinityProcess();
        }


        return process;
    }

    @Override
    public UProcess launchLocally( RavenTaskInstance instance ) {
        return null;
    }

    @Override
    public UProcess launchRemotely( RavenTaskInstance instance, long pmClientId ) {
        return null;
    }

    @Override
    public UProcess elevate( RavenTaskInstance instance ) {
        return null;
    }

    @Override
    public UProcess elevateLocally( RavenTaskInstance instance ) {
        return null;
    }

    @Override
    public UProcess elevateRemotely( RavenTaskInstance instance, long pmClientId ) {
        return null;
    }

}
