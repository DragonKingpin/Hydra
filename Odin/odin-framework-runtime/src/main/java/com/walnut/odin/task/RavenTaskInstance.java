package com.walnut.odin.task;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.task.TaskInstance;

public interface RavenTaskInstance extends TaskInstance {

    UProcess affinityProcess();

    void startLocalProcess();

    void startRemoteProcess();

    void startRemoteProcess( boolean bDirectlyVitalize );

    void startRemoteProcess( boolean bDirectlyVitalize, long processClientId );

}
