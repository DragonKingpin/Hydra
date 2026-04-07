package com.walnut.odin.conduct.schedule.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;

public class DepartureChecklist implements Pinenut {

    private boolean             mbTraceDependencyDetails; // 是否追踪依赖明细，开启会增加内存消耗，会记录依赖实例.
    private Collection<GUID>    mDependentInstanceIds;

    private LocalDateTime       mCheckTime;
    private InstanceEntry       mTargetInstance;
    private TaskInstanceStatus  mInterceptedStatus;       // 拦截状态原因.
    private TaskInstanceStatus  mPreDepartureLastStatus;  // 启动前最后状态，DepartureStandby状态才能出港，其他状态送入下一批流水线.


    public DepartureChecklist( InstanceEntry targetInstance, boolean bTraceDependencyDetails ) {
        this.mTargetInstance = targetInstance;
        this.mbTraceDependencyDetails = bTraceDependencyDetails;
        this.mCheckTime = LocalDateTime.now();
    }

    public DepartureChecklist( InstanceEntry targetInstance ) {
        this( targetInstance, true );
    }

    public boolean isTraceDependencyDetails() {
        return this.mbTraceDependencyDetails;
    }

    public void setTraceDependencyDetails( boolean bTraceDependencyDetails ) {
        this.mbTraceDependencyDetails = bTraceDependencyDetails;
    }

    public Collection<GUID> getDependentInstanceIds() {
        return this.mDependentInstanceIds;
    }

    public void setDependentInstanceIds( Collection<GUID> dependentInstanceIds ) {
        this.mDependentInstanceIds = dependentInstanceIds;
    }

    public InstanceEntry getTargetInstance() {
        return this.mTargetInstance;
    }

    public void setTargetInstance( InstanceEntry targetInstance ) {
        this.mTargetInstance = targetInstance;
    }

    public TaskInstanceStatus getInterceptedStatus() {
        return this.mInterceptedStatus;
    }

    public void setInterceptedStatus( TaskInstanceStatus interceptedStatus ) {
        this.mInterceptedStatus = interceptedStatus;
    }

    public TaskInstanceStatus getPreDepartureLastStatus() {
        return this.mPreDepartureLastStatus;
    }

    public void setPreDepartureLastStatus( TaskInstanceStatus preDepartureLastStatus ) {
        this.mPreDepartureLastStatus = preDepartureLastStatus;
    }

    public boolean isDepartureCheckPassed() {
        return this.mPreDepartureLastStatus == TaskInstanceStatus.DepartureStandby;
    }

    public boolean isIntercepted() {
        return this.mInterceptedStatus != null;
    }

    public void addDependentInstanceId( GUID dependentInstanceId ) {
        if ( !this.mbTraceDependencyDetails ) {
            return;
        }

        if ( this.mDependentInstanceIds == null ) {
            this.mDependentInstanceIds = new ArrayList<>();
        }

        this.mDependentInstanceIds.add( dependentInstanceId );
    }

    public LocalDateTime getCheckTime() {
        return this.mCheckTime;
    }

    public void setCheckTime( LocalDateTime checkTime ) {
        this.mCheckTime = checkTime;
    }

}
