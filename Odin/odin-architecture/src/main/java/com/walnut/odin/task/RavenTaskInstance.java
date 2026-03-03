package com.walnut.odin.task;

import java.net.URI;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstance;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;

public interface RavenTaskInstance extends TaskInstance {

    URI getProcessImageURI();

    UProcess affinityProcess();

    void startLocalProcess();

    void startRemoteProcess();

    void startRemoteProcess( boolean bDirectlyVitalize );

    void startRemoteProcess( boolean bDirectlyVitalize, long processClientId );

    void update() throws MetaPersistenceException;

    void persist() throws MetaPersistenceException;

    InstanceInstrument instanceInstrument();

}
