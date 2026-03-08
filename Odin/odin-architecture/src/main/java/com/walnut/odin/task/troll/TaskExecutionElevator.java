package com.walnut.odin.task.troll;

import java.time.LocalDateTime;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;

public interface TaskExecutionElevator extends Manager {

    ProcessManager processManager();

    LocalDateTime evalBusinessTime( RavenTaskInstance instance, LocalDateTime biz ) ;

    LocalDateTime evalBusinessTime( RavenTaskInstance instance ) ;

    String evalBusinessTimeLabel( RavenTaskInstance instance, LocalDateTime biz ) ;

    String evalBusinessTimeLabel( RavenTaskInstance instance ) ;

    String evalInstanceName( RavenTaskInstance instance, LocalDateTime now, LocalDateTime bizTimeEpoch ) ;

    String evalInstanceName( RavenTaskInstance instance, LocalDateTime bizTimeEpoch ) ;



    UProcess launchLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess launchRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;



    UProcess elevateLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess elevateRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;
}
