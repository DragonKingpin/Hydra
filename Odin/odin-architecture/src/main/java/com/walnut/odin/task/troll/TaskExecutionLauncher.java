package com.walnut.odin.task.troll;

import java.time.LocalDateTime;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;

public interface TaskExecutionLauncher extends Manager {

    ProcessManager processManager();

    LocalDateTime evalBusinessTime( RavenTaskInstance instance, LocalDateTime biz ) ;

    LocalDateTime evalBusinessTime( RavenTaskInstance instance ) ;

    String evalBusinessTimeLabel( RavenTaskInstance instance, LocalDateTime biz ) ;

    String evalBusinessTimeLabel( RavenTaskInstance instance ) ;

    String evalInstanceName( RavenTaskInstance instance, LocalDateTime now, LocalDateTime bizTimeEpoch ) ;

    String evalInstanceName( RavenTaskInstance instance, LocalDateTime bizTimeEpoch ) ;

    void initializeInstance( RavenTaskInstance instance, LaunchFeature feature );



    UProcess createLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess createRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess createPreparedLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess createPreparedRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;



    UProcess launchLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess launchRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess launchPreparedLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess launchPreparedRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess startLocally( RavenTaskInstance instance, UProcess process, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess startRemotely( RavenTaskInstance instance, UProcess process, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException;
}
