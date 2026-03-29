package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class ConcurrentQuota implements Pinenut {

    private final short      mnPriority;

    // 最大水位，如果高于该水位，对应优先级的任务不允许再并行启动
    // The maximum level, if it is higher than this level,
    // tasks of corresponding priority are not allowed to start in parallel again
    private volatile double  mnMaximumRatio;
    private volatile Long    mnMaximumCnt;


    // 最低保障水位，如果低于该水位，会主动启动对应优先级的任务
    // The minimum guaranteed level,
    // if it is lower than this level, the corresponding priority task will be initiated actively
    private volatile double  mnMinimumRatio;
    private volatile Long    mnMinimumCnt;

    public ConcurrentQuota( short priority ) {
        this.mnPriority = priority;
    }

    public short getPriority() {
        return this.mnPriority;
    }

    public double getMaximumRatio() {
        return this.mnMaximumRatio;
    }

    public void setMaximumRatio( double nMaximumRatio ) {
        this.mnMaximumRatio = nMaximumRatio;
    }

    public long getMaximumCnt() {
        return this.mnMaximumCnt;
    }

    public void setMaximumCnt( long nMaximumCnt ) {
        this.mnMaximumCnt = nMaximumCnt;
    }

    public double getMinimumRatio() {
        return this.mnMinimumRatio;
    }

    public void setMinimumRatio( double nMinimumRatio ) {
        this.mnMinimumRatio = nMinimumRatio;
    }

    public long getMinimumCnt() {
        return this.mnMinimumCnt;
    }

    public void setMinimumCnt( long nMinimumCnt ) {
        this.mnMinimumCnt = nMinimumCnt;
    }
}
