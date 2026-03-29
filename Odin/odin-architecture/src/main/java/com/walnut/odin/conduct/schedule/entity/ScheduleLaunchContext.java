package com.walnut.odin.conduct.schedule.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;

public class ScheduleLaunchContext implements Pinenut {

    private Collection<InstanceEntry> mLaunchedInstances;
    private Collection<InstanceEntry> mDiscardedInstances;

    public ScheduleLaunchContext() {
        this.mLaunchedInstances  = new ArrayList<>();
        this.mDiscardedInstances = new ArrayList<>();
    }

    public Collection<InstanceEntry> getLaunchedInstances() {
        return this.mLaunchedInstances;
    }

    public void setLaunchedInstances( Collection<InstanceEntry> launchedInstances ) {
        this.mLaunchedInstances = launchedInstances;
    }

    public Collection<InstanceEntry> getDiscardedInstances() {
        return this.mDiscardedInstances;
    }

    public void setDiscardedInstances( Collection<InstanceEntry> discardedInstances ) {
        this.mDiscardedInstances = discardedInstances;
    }

}