package com.walnut.odin.formation.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class FormationRuntimeSnapshot implements Pinenut {
    protected FormationSchedulerRuntimeSnapshot  mSchedulerSnapshot;
    protected FormationDispatcherRuntimeSnapshot mDispatcherSnapshot;

    public FormationSchedulerRuntimeSnapshot getSchedulerSnapshot() { return this.mSchedulerSnapshot; }
    public void setSchedulerSnapshot( FormationSchedulerRuntimeSnapshot schedulerSnapshot ) { this.mSchedulerSnapshot = schedulerSnapshot; }
    public FormationDispatcherRuntimeSnapshot getDispatcherSnapshot() { return this.mDispatcherSnapshot; }
    public void setDispatcherSnapshot( FormationDispatcherRuntimeSnapshot dispatcherSnapshot ) { this.mDispatcherSnapshot = dispatcherSnapshot; }
}
