package com.walnut.odin.task.troll;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;

public interface TaskExecutionElevator extends Manager {

    ProcessManager processManager();

    UProcess launch( RavenTaskInstance instance );

    UProcess launchLocally( RavenTaskInstance instance );

    UProcess launchRemotely( RavenTaskInstance instance, long pmClientId );

    UProcess elevate( RavenTaskInstance instance );

    UProcess elevateLocally( RavenTaskInstance instance );

    UProcess elevateRemotely( RavenTaskInstance instance, long pmClientId );
}
