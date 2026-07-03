package com.walnut.odin.atlas.deletion;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskPurgeResult implements Pinenut {

    protected TaskPurgeSafetyReport safetyReport;
    protected List<String>          warnings = new ArrayList<>();
    protected int                   signaledProcessCount;
    protected int                   removedTaskLineageCount;
    protected int                   removedInstanceLineageEdgeCount;
    protected int                   removedExecAuditCount;
    protected int                   removedPatrolLogCount;
    protected int                   removedOperationLogCount;
    protected int                   removedInstanceEventCount;
    protected int                   removedExecCount;
    protected int                   removedInstanceCount;
    protected int                   removedTaskCount;

    public TaskPurgeSafetyReport getSafetyReport() {
        return this.safetyReport;
    }

    public void setSafetyReport( TaskPurgeSafetyReport safetyReport ) {
        this.safetyReport = safetyReport;
    }

    public List<String> getWarnings() {
        return this.warnings;
    }

    public void setWarnings( List<String> warnings ) {
        this.warnings = warnings;
    }

    public int getSignaledProcessCount() {
        return this.signaledProcessCount;
    }

    public void setSignaledProcessCount( int signaledProcessCount ) {
        this.signaledProcessCount = signaledProcessCount;
    }

    public int getRemovedTaskLineageCount() {
        return this.removedTaskLineageCount;
    }

    public void setRemovedTaskLineageCount( int removedTaskLineageCount ) {
        this.removedTaskLineageCount = removedTaskLineageCount;
    }

    public int getRemovedInstanceLineageEdgeCount() {
        return this.removedInstanceLineageEdgeCount;
    }

    public void setRemovedInstanceLineageEdgeCount( int removedInstanceLineageEdgeCount ) {
        this.removedInstanceLineageEdgeCount = removedInstanceLineageEdgeCount;
    }

    public int getRemovedExecAuditCount() {
        return this.removedExecAuditCount;
    }

    public void setRemovedExecAuditCount( int removedExecAuditCount ) {
        this.removedExecAuditCount = removedExecAuditCount;
    }

    public int getRemovedPatrolLogCount() {
        return this.removedPatrolLogCount;
    }

    public void setRemovedPatrolLogCount( int removedPatrolLogCount ) {
        this.removedPatrolLogCount = removedPatrolLogCount;
    }

    public int getRemovedOperationLogCount() {
        return this.removedOperationLogCount;
    }

    public void setRemovedOperationLogCount( int removedOperationLogCount ) {
        this.removedOperationLogCount = removedOperationLogCount;
    }

    public int getRemovedInstanceEventCount() {
        return this.removedInstanceEventCount;
    }

    public void setRemovedInstanceEventCount( int removedInstanceEventCount ) {
        this.removedInstanceEventCount = removedInstanceEventCount;
    }

    public int getRemovedExecCount() {
        return this.removedExecCount;
    }

    public void setRemovedExecCount( int removedExecCount ) {
        this.removedExecCount = removedExecCount;
    }

    public int getRemovedInstanceCount() {
        return this.removedInstanceCount;
    }

    public void setRemovedInstanceCount( int removedInstanceCount ) {
        this.removedInstanceCount = removedInstanceCount;
    }

    public int getRemovedTaskCount() {
        return this.removedTaskCount;
    }

    public void setRemovedTaskCount( int removedTaskCount ) {
        this.removedTaskCount = removedTaskCount;
    }
}
