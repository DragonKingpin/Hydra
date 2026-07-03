package com.walnut.odin.formation.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class FormationDispatcherRuntimeSnapshot implements Pinenut {
    protected boolean mbRunning;
    protected boolean mbEnabled;
    protected int     mnWorkerThreadCount;
    protected int     mnQueueCapacity;
    protected int     mnQueueSize;
    protected int     mnRemainingQueueCapacity;
    protected int     mnPollBatchSize;
    protected long    mnOfferTimeoutMillis;
    protected long    mnIdleSleepMillis;
    protected long    mnAcceptedCount;
    protected long    mnCompletedCount;
    protected long    mnFailedCount;
    protected long    mnRejectedCount;

    public boolean isRunning() {
        return this.mbRunning;
    }

    public void setRunning( boolean running ) {
        this.mbRunning = running;
    }

    public boolean isEnabled() {
        return this.mbEnabled;
    }

    public void setEnabled( boolean enabled ) {
        this.mbEnabled = enabled;
    }

    public int getWorkerThreadCount() {
        return this.mnWorkerThreadCount;
    }

    public void setWorkerThreadCount( int workerThreadCount ) {
        this.mnWorkerThreadCount = workerThreadCount;
    }

    public int getQueueCapacity() {
        return this.mnQueueCapacity;
    }

    public void setQueueCapacity( int queueCapacity ) {
        this.mnQueueCapacity = queueCapacity;
    }

    public int getQueueSize() {
        return this.mnQueueSize;
    }

    public void setQueueSize( int queueSize ) {
        this.mnQueueSize = queueSize;
    }

    public int getRemainingQueueCapacity() {
        return this.mnRemainingQueueCapacity;
    }

    public void setRemainingQueueCapacity( int remainingQueueCapacity ) {
        this.mnRemainingQueueCapacity = remainingQueueCapacity;
    }

    public int getPollBatchSize() {
        return this.mnPollBatchSize;
    }

    public void setPollBatchSize( int pollBatchSize ) {
        this.mnPollBatchSize = pollBatchSize;
    }

    public long getOfferTimeoutMillis() {
        return this.mnOfferTimeoutMillis;
    }

    public void setOfferTimeoutMillis( long offerTimeoutMillis ) {
        this.mnOfferTimeoutMillis = offerTimeoutMillis;
    }

    public long getIdleSleepMillis() {
        return this.mnIdleSleepMillis;
    }

    public void setIdleSleepMillis( long idleSleepMillis ) {
        this.mnIdleSleepMillis = idleSleepMillis;
    }

    public long getAcceptedCount() {
        return this.mnAcceptedCount;
    }

    public void setAcceptedCount( long acceptedCount ) {
        this.mnAcceptedCount = acceptedCount;
    }

    public long getCompletedCount() {
        return this.mnCompletedCount;
    }

    public void setCompletedCount( long completedCount ) {
        this.mnCompletedCount = completedCount;
    }

    public long getFailedCount() {
        return this.mnFailedCount;
    }

    public void setFailedCount( long failedCount ) {
        this.mnFailedCount = failedCount;
    }

    public long getRejectedCount() {
        return this.mnRejectedCount;
    }

    public void setRejectedCount( long rejectedCount ) {
        this.mnRejectedCount = rejectedCount;
    }
}
