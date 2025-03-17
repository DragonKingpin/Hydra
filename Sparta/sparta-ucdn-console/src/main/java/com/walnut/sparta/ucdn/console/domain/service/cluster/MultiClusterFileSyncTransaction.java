package com.walnut.sparta.ucdn.console.domain.service.cluster;

import java.util.concurrent.atomic.AtomicInteger;

public class MultiClusterFileSyncTransaction implements ClusterFileSyncTransaction {
    private int             clusterNodeCount;

    private AtomicInteger   remainingCount;

    public MultiClusterFileSyncTransaction( int clusterNodeCount ) {
        this.clusterNodeCount = clusterNodeCount;
        this.remainingCount = new AtomicInteger( clusterNodeCount );
    }

    public MultiClusterFileSyncTransaction() {
    }

    @Override
    public int getClusterNodeCount() {
        return clusterNodeCount;
    }

    @Override
    public void setClusterNodeCount( int clusterNodeCount ) {
        this.clusterNodeCount = clusterNodeCount;
    }

    @Override
    public int checkRemainingCount() {
        return this.remainingCount.getAcquire();
    }

    @Override
    public int decreaseRemainingCount() {
        return this.remainingCount.decrementAndGet();
    }
}
