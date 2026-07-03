package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class ScheduleAllocatorQuotaSnapshot implements Pinenut {

    protected String  mName;
    protected short   mPriority;
    protected double  mMaximumRatio;
    protected double  mMinimumRatio;
    protected long    mMaximumCnt;
    protected long    mMinimumCnt;
    protected long    mCurrentCnt;
    protected boolean mMaximumRatioMode;
    protected boolean mMinimumRatioMode;
    protected boolean mMaximumUnlimited;
    protected boolean mMinimumUnlimited;
    protected boolean mFull;

    public String getName() {
        return this.mName;
    }

    public void setName( String name ) {
        this.mName = name;
    }

    public short getPriority() {
        return this.mPriority;
    }

    public void setPriority( short priority ) {
        this.mPriority = priority;
    }

    public double getMaximumRatio() {
        return this.mMaximumRatio;
    }

    public void setMaximumRatio( double maximumRatio ) {
        this.mMaximumRatio = maximumRatio;
    }

    public double getMinimumRatio() {
        return this.mMinimumRatio;
    }

    public void setMinimumRatio( double minimumRatio ) {
        this.mMinimumRatio = minimumRatio;
    }

    public long getMaximumCnt() {
        return this.mMaximumCnt;
    }

    public void setMaximumCnt( long maximumCnt ) {
        this.mMaximumCnt = maximumCnt;
    }

    public long getMinimumCnt() {
        return this.mMinimumCnt;
    }

    public void setMinimumCnt( long minimumCnt ) {
        this.mMinimumCnt = minimumCnt;
    }

    public long getCurrentCnt() {
        return this.mCurrentCnt;
    }

    public void setCurrentCnt( long currentCnt ) {
        this.mCurrentCnt = currentCnt;
    }

    public boolean isMaximumRatioMode() {
        return this.mMaximumRatioMode;
    }

    public void setMaximumRatioMode( boolean maximumRatioMode ) {
        this.mMaximumRatioMode = maximumRatioMode;
    }

    public boolean isMinimumRatioMode() {
        return this.mMinimumRatioMode;
    }

    public void setMinimumRatioMode( boolean minimumRatioMode ) {
        this.mMinimumRatioMode = minimumRatioMode;
    }

    public boolean isMaximumUnlimited() {
        return this.mMaximumUnlimited;
    }

    public void setMaximumUnlimited( boolean maximumUnlimited ) {
        this.mMaximumUnlimited = maximumUnlimited;
    }

    public boolean isMinimumUnlimited() {
        return this.mMinimumUnlimited;
    }

    public void setMinimumUnlimited( boolean minimumUnlimited ) {
        this.mMinimumUnlimited = minimumUnlimited;
    }

    public boolean isFull() {
        return this.mFull;
    }

    public void setFull( boolean full ) {
        this.mFull = full;
    }
}
