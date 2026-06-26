package com.walnut.odin.conduct.schedule.entity;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class ScheduleAllocatorSnapshot implements Pinenut {

    protected String                               mPartitionName;
    protected long                                 mGlobalConcurrentInstance;
    protected long                                 mCurrentInstanceCount;
    protected List<Integer>                        mFulledPriorities = new ArrayList<>();
    protected List<ScheduleAllocatorQuotaSnapshot> mQuotas           = new ArrayList<>();

    public String getPartitionName() {
        return this.mPartitionName;
    }

    public void setPartitionName( String partitionName ) {
        this.mPartitionName = partitionName;
    }

    public long getGlobalConcurrentInstance() {
        return this.mGlobalConcurrentInstance;
    }

    public void setGlobalConcurrentInstance( long globalConcurrentInstance ) {
        this.mGlobalConcurrentInstance = globalConcurrentInstance;
    }

    public long getCurrentInstanceCount() {
        return this.mCurrentInstanceCount;
    }

    public void setCurrentInstanceCount( long currentInstanceCount ) {
        this.mCurrentInstanceCount = currentInstanceCount;
    }

    public List<Integer> getFulledPriorities() {
        return this.mFulledPriorities;
    }

    public void setFulledPriorities( List<Integer> fulledPriorities ) {
        this.mFulledPriorities = fulledPriorities == null ? new ArrayList<>() : fulledPriorities;
    }

    public List<ScheduleAllocatorQuotaSnapshot> getQuotas() {
        return this.mQuotas;
    }

    public void setQuotas( List<ScheduleAllocatorQuotaSnapshot> quotas ) {
        this.mQuotas = quotas == null ? new ArrayList<>() : quotas;
    }
}
