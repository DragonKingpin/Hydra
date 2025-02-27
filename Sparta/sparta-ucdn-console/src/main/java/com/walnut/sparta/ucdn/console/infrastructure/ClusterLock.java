package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.concurrent.atomic.AtomicInteger;

public class ClusterLock implements Pinenut {
    private AtomicInteger waitThreatNum;

    public ClusterLock(){
        this.waitThreatNum = new AtomicInteger(0);
    }

    public AtomicInteger getWaitThreatNum(){
        return this.waitThreatNum;
    }

    public void increment(){
        this.waitThreatNum.getAndIncrement();
    }

    public void decrement(){
        this.waitThreatNum.getAndDecrement();
    }

 }
