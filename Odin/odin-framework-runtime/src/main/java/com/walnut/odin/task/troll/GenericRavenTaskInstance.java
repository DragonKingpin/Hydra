package com.walnut.odin.task.troll;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.task.ArchTaskInstance;
import com.pinecone.hydra.task.Task;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.task.RavenTaskInstance;

public class GenericRavenTaskInstance extends ArchRavenTaskInstance implements RavenTaskInstance {

    public GenericRavenTaskInstance( InstanceEntry instanceEntry ) {
        super( instanceEntry );
    }



    @Override
    public Object getProcessObject() {
        return null;
    }

    @Override
    public Task getAffiliatedTask() {
        return null;
    }

    @Override
    public UProcess affinityProcess() {
        return null;
    }

    @Override
    public void startLocalProcess() {

    }

    @Override
    public void startRemoteProcess() {

    }

    @Override
    public void startRemoteProcess( boolean bDirectlyVitalize ) {

    }

    @Override
    public void startRemoteProcess( boolean bDirectlyVitalize, long processClientId ) {

    }
}
