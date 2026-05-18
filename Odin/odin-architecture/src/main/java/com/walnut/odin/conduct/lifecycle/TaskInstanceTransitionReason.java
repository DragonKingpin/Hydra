package com.walnut.odin.conduct.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.InstanceEventType;

public enum TaskInstanceTransitionReason implements Pinenut {

    TimeReady        ( InstanceEventType.TaskTimeReady.getName()        ),
    DependencyReady  ( InstanceEventType.CheckDependencyReady.getName() ),
    DepartureReady   ( InstanceEventType.DepartureReady.getName()       ),
    ProcessCreationClaim  ( InstanceEventType.DepartureReady.getName()  ),
    ProcessCreated   ( InstanceEventType.DepartureReady.getName()       ),
    ProcessCreationFailed ( InstanceEventType.TaskFail.getName()        ),
    ProcessStarted   ( InstanceEventType.TaskRun.getName()              ),
    ProcessSucceeded ( InstanceEventType.TaskSuccess.getName()          ),
    ProcessFailed    ( InstanceEventType.TaskFail.getName()             ),
    ProcessKilled    ( InstanceEventType.TaskKilled.getName()           ),
    Manual           ( "Manual"                                         );

    private final String mszEventType;

    TaskInstanceTransitionReason( String szEventType ) {
        this.mszEventType = szEventType;
    }

    public String getEventType() {
        return this.mszEventType;
    }
}
