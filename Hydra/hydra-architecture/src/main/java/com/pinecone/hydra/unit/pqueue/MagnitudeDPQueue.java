package com.pinecone.hydra.unit.pqueue;

import com.pinecone.hydra.unit.pqueue.entity.QueueElement;

public class MagnitudeDPQueue implements MegaDeflectPriorityQueue {
    private DPQueueManipulator mDPQueueManipulator;

    public MagnitudeDPQueue( DPQueueManipulator dpQueueManipulator ) {
        this.mDPQueueManipulator = dpQueueManipulator;
    }

    @Override
    public String getSharedSegmentField() {
        return "";
    }

    @Override
    public String getSharedSegmentName() {
        return "";
    }

    @Override
    public void add(QueueElement queueElement) {
        this.mDPQueueManipulator.add( queueElement );
    }

    @Override
    public QueueElement poll() {
        return this.mDPQueueManipulator.poll();
    }

    @Override
    public QueueElement peek() {
        return this.mDPQueueManipulator.peek();
    }

    @Override
    public long size() {
        return this.mDPQueueManipulator.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mDPQueueManipulator.size() == 0;
    }

    @Override
    public boolean contains(QueueElement queueElement) {
        return false;
    }
}
