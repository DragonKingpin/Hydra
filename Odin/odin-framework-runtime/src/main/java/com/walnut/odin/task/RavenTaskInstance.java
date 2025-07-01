package com.walnut.odin.task;

import java.net.URI;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.task.TaskInstance;

public interface RavenTaskInstance extends TaskInstance {

    URI getProcessImageURI();

    UProcess affinityProcess();

    void startLocalProcess();

    void startRemoteProcess();

    void startRemoteProcess( boolean bDirectlyVitalize );

    void startRemoteProcess( boolean bDirectlyVitalize, long processClientId );

}
