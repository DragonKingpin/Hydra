package com.walnut.odin.task.troll;

import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;

public class TrollTaskExecutionElevator implements TaskExecutionElevator {



    @Override
    public ProcessManager processManager() {
        return null;
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
    public void elevate( RavenTaskInstance instance ) {

    }


}
