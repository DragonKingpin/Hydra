package com.walnut.odin.conduct.schedule.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.dispatch.TaskLaunchContext;

public class InstanceDepartureResult implements Pinenut {

    protected Collection<DepartureChecklist> mChecklists;
    protected Collection<InstanceEntry>      mResourceWaitInstances;
    protected Collection<InstanceEntry>      mDepartureStandbyInstances;
    protected Collection<InstanceEntry>      mClaimedInstances;
    protected Collection<InstanceEntry>      mDiscardedInstances;
    protected Collection<TaskLaunchContext>  mLaunchContexts;

    public InstanceDepartureResult() {
        this.mChecklists                 = new ArrayList<>();
        this.mResourceWaitInstances      = new ArrayList<>();
        this.mDepartureStandbyInstances  = new ArrayList<>();
        this.mClaimedInstances           = new ArrayList<>();
        this.mDiscardedInstances         = new ArrayList<>();
        this.mLaunchContexts             = new ArrayList<>();
    }

    public Collection<DepartureChecklist> getChecklists() {
        return this.mChecklists;
    }

    public void setChecklists( Collection<DepartureChecklist> checklists ) {
        this.mChecklists = checklists;
    }

    public Collection<InstanceEntry> getResourceWaitInstances() {
        return this.mResourceWaitInstances;
    }

    public void setResourceWaitInstances( Collection<InstanceEntry> resourceWaitInstances ) {
        this.mResourceWaitInstances = resourceWaitInstances;
    }

    public Collection<InstanceEntry> getDepartureStandbyInstances() {
        return this.mDepartureStandbyInstances;
    }

    public void setDepartureStandbyInstances( Collection<InstanceEntry> departureStandbyInstances ) {
        this.mDepartureStandbyInstances = departureStandbyInstances;
    }

    public Collection<InstanceEntry> getClaimedInstances() {
        return this.mClaimedInstances;
    }

    public void setClaimedInstances( Collection<InstanceEntry> claimedInstances ) {
        this.mClaimedInstances = claimedInstances;
    }

    public Collection<InstanceEntry> getDiscardedInstances() {
        return this.mDiscardedInstances;
    }

    public void setDiscardedInstances( Collection<InstanceEntry> discardedInstances ) {
        this.mDiscardedInstances = discardedInstances;
    }

    public Collection<TaskLaunchContext> getLaunchContexts() {
        return this.mLaunchContexts;
    }

    public void setLaunchContexts( Collection<TaskLaunchContext> launchContexts ) {
        this.mLaunchContexts = launchContexts;
    }

}
