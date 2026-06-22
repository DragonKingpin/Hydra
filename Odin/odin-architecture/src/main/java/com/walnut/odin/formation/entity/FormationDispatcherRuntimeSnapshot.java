package com.walnut.odin.formation.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class FormationDispatcherRuntimeSnapshot implements Pinenut {
    protected boolean mbRunning;
    protected int     mnWorkerThreadCount;
    protected int     mnQueueSize;
    protected long    mnAcceptedCount;
    protected long    mnCompletedCount;
    protected long    mnFailedCount;
    protected long    mnRejectedCount;

    public boolean isRunning() { return this.mbRunning; }
    public void setRunning( boolean running ) { this.mbRunning = running; }
    public int getWorkerThreadCount() { return this.mnWorkerThreadCount; }
    public void setWorkerThreadCount( int workerThreadCount ) { this.mnWorkerThreadCount = workerThreadCount; }
    public int getQueueSize() { return this.mnQueueSize; }
    public void setQueueSize( int queueSize ) { this.mnQueueSize = queueSize; }
    public long getAcceptedCount() { return this.mnAcceptedCount; }
    public void setAcceptedCount( long acceptedCount ) { this.mnAcceptedCount = acceptedCount; }
    public long getCompletedCount() { return this.mnCompletedCount; }
    public void setCompletedCount( long completedCount ) { this.mnCompletedCount = completedCount; }
    public long getFailedCount() { return this.mnFailedCount; }
    public void setFailedCount( long failedCount ) { this.mnFailedCount = failedCount; }
    public long getRejectedCount() { return this.mnRejectedCount; }
    public void setRejectedCount( long rejectedCount ) { this.mnRejectedCount = rejectedCount; }
}
