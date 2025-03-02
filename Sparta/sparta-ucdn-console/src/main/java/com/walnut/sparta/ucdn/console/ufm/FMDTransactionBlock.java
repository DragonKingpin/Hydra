package com.walnut.sparta.ucdn.console.ufm;

public class FMDTransactionBlock {
    private Object clusterLock;

    private Long clusterCompletedCount;

    private Long consumerCompletedCount;

    public FMDTransactionBlock(){}

    public FMDTransactionBlock(Object clusterLock, Long clusterCompletedCount, Long consumerCompletedCount) {
        this.clusterLock = clusterLock;
        this.clusterCompletedCount = clusterCompletedCount;
        this.consumerCompletedCount = consumerCompletedCount;
    }

    public Object getClusterLock() {
        return this.clusterLock;
    }

    public void setClusterLock(Object clusterLock) {
        this.clusterLock = clusterLock;
    }

    public Long getClusterCompletedCount() {
        return this.clusterCompletedCount;
    }

    public void setClusterCompletedCount(Long clusterCompletedCount) {
        this.clusterCompletedCount = clusterCompletedCount;
    }

    public Long getConsumerCompletedCount() {
        return this.consumerCompletedCount;
    }

    public void setConsumerCompletedCount(Long consumerCompletedCount) {
        this.consumerCompletedCount = consumerCompletedCount;
    }
}
