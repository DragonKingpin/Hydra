package com.walnut.odin.dispatch;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.LaunchFeature;

public interface TaskLaunchContext extends Pinenut {

    LaunchFeature getLaunchFeature();

    RavenTaskInstance getTaskInstance();

    default Identification getTaskId() {
        return this.getTaskInstance().getOwnedTask().getId();
    }

    default Identification getTaskInstanceId() {
        return this.getTaskInstance().getId();
    }


    String getAffinityProcessorName();

    void setAffinityProcessorName( String affinityProcessorName );

    UProcess getLaunchedProcess();

    void afterProcessLaunched( UProcess launchedProcess );


    static TaskLaunchContext of( RavenTaskInstance taskInstance, LaunchFeature launchFeature ) {
        return new TaskLaunchContext() {
            private String affinityProcessorName;
            private UProcess launchedProcess;

            @Override
            public LaunchFeature getLaunchFeature() {
                return launchFeature;
            }

            @Override
            public RavenTaskInstance getTaskInstance() {
                return taskInstance;
            }

            @Override
            public String getAffinityProcessorName() {
                return this.affinityProcessorName;
            }

            @Override
            public void setAffinityProcessorName( String affinityProcessorName ) {
                this.affinityProcessorName = affinityProcessorName;
            }

            @Override
            public UProcess getLaunchedProcess() {
                return this.launchedProcess;
            }

            @Override
            public void afterProcessLaunched( UProcess launchedProcess ) {
                this.launchedProcess = launchedProcess;
            }
        };
    }

}
