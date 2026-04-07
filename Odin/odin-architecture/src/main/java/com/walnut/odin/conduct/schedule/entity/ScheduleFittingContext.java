package com.walnut.odin.conduct.schedule.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;

public class ScheduleFittingContext implements Pinenut {

    private Collection<InstanceEntry> mFittedInstances;
    private Collection<InstanceEntry> mDiscardedInstances;

    public ScheduleFittingContext() {
        this.mFittedInstances    = new ArrayList<>();
        this.mDiscardedInstances = new ArrayList<>();
    }

    public Collection<InstanceEntry> getFittedInstances() {
        return this.mFittedInstances;
    }

    public void setFittedInstances( Collection<InstanceEntry> launchedInstances ) {
        this.mFittedInstances = launchedInstances;
    }

    public Collection<InstanceEntry> getDiscardedInstances() {
        return this.mDiscardedInstances;
    }

    public void setDiscardedInstances( Collection<InstanceEntry> discardedInstances ) {
        this.mDiscardedInstances = discardedInstances;
    }

}