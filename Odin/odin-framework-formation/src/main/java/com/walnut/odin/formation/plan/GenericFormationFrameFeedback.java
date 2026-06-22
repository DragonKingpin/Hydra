package com.walnut.odin.formation.plan;

import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;

public class GenericFormationFrameFeedback implements FormationFrameFeedback {
    protected FormationFrame                mFrame;
    protected TaskInstantaneousSubmitResult mSubmitResult;
    protected boolean                       mbAccepted;
    protected boolean                       mbSuspended;
    protected boolean                       mbRejected;

    public GenericFormationFrameFeedback() {
    }

    public GenericFormationFrameFeedback( FormationFrame frame, TaskInstantaneousSubmitResult submitResult, boolean accepted ) {
        this.mFrame = frame;
        this.mSubmitResult = submitResult;
        this.mbAccepted = accepted;
        this.mbRejected = !accepted;
    }

    @Override
    public FormationFrame frame() { return this.mFrame; }
    public void setFrame( FormationFrame frame ) { this.mFrame = frame; }
    @Override
    public TaskInstantaneousSubmitResult submitResult() { return this.mSubmitResult; }
    public void setSubmitResult( TaskInstantaneousSubmitResult submitResult ) { this.mSubmitResult = submitResult; }
    @Override
    public boolean accepted() { return this.mbAccepted; }
    public void setAccepted( boolean accepted ) { this.mbAccepted = accepted; }
    @Override
    public boolean suspended() { return this.mbSuspended; }
    public void setSuspended( boolean suspended ) { this.mbSuspended = suspended; }
    @Override
    public boolean rejected() { return this.mbRejected; }
    public void setRejected( boolean rejected ) { this.mbRejected = rejected; }
}
