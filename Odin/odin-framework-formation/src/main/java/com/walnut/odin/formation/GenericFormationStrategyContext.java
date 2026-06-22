package com.walnut.odin.formation;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;

public class GenericFormationStrategyContext implements FormationStrategyContext {
    protected GUID                 mFormationGuid;
    protected GUID                 mGroupGuid;
    protected UniformTaskScheduler mTaskScheduler;

    public GenericFormationStrategyContext() {
    }

    public GenericFormationStrategyContext( GUID formationGuid, GUID groupGuid, UniformTaskScheduler taskScheduler ) {
        this.mFormationGuid = formationGuid;
        this.mGroupGuid = groupGuid;
        this.mTaskScheduler = taskScheduler;
    }

    @Override
    public GUID formationGuid() {
        return this.mFormationGuid;
    }

    public GUID getFormationGuid() {
        return this.mFormationGuid;
    }

    public void setFormationGuid( GUID formationGuid ) {
        this.mFormationGuid = formationGuid;
    }

    @Override
    public GUID groupGuid() {
        return this.mGroupGuid;
    }

    public GUID getGroupGuid() {
        return this.mGroupGuid;
    }

    public void setGroupGuid( GUID groupGuid ) {
        this.mGroupGuid = groupGuid;
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }

    public UniformTaskScheduler getTaskScheduler() {
        return this.mTaskScheduler;
    }

    public void setTaskScheduler( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler = taskScheduler;
    }
}
