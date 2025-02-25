package com.walnut.sparta.ucdn.console.infrastructure;

import java.util.concurrent.atomic.AtomicInteger;

public class UCDNSyncTransaction implements SyncTransaction {
    private int             nodeNum;

    private AtomicInteger   remainingNum;

    public UCDNSyncTransaction( int nodeNum) {
        this.nodeNum = nodeNum;
        this.remainingNum = new AtomicInteger(nodeNum);
    }

    public UCDNSyncTransaction() {
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }

    @Override
    public int checkRemainingNum() {
        return this.remainingNum.getAcquire();
    }

    @Override
    public int decreaseRemainingNum() {
        return this.remainingNum.decrementAndGet();
    }
}
