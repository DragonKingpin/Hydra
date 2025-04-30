package com.pinecone.hydra.unit.pqueue;

import com.pinecone.hydra.unit.pqueue.entity.QueueElement;

public class MagnitudeDPQueue implements MegaDeflectPriorityQueue, Cloneable {
    private DPQueueManipulator      mDPQueueManipulator;

    private long                    mnCurrentPos;

    private String                  mszSharedSegmentField;

    private String                  mszSharedSegmentName;

    private QueueMeta               mQueueMeta;

    public MagnitudeDPQueue( DPQueueManipulator dpQueueManipulator, long currentPos,
                             String shareSegmentField, String sharedSegmentName,QueueMeta queueMeta ) {
        this.mDPQueueManipulator        = dpQueueManipulator;
        this.mnCurrentPos               = currentPos;
        this.mszSharedSegmentField      = shareSegmentField;
        this.mszSharedSegmentName       = sharedSegmentName;
        this.mQueueMeta                 = queueMeta;
    }

    @Override
    public String getSharedSegmentField() {
        return this.mszSharedSegmentField;
    }

    @Override
    public String getSharedSegmentName() {
        return this.mszSharedSegmentName;
    }

    @Override
    public void setCurrentPos( long mnCurrentPos ) {
        this.mnCurrentPos = mnCurrentPos;
    }

    @Override
    public void reset() {
        this.setCurrentPos( 0 );
    }

    @Override
    public MagnitudeDPQueue clone() {
        try {
            Object o = super.clone();
            MagnitudeDPQueue neo = (MagnitudeDPQueue) o;
            return neo;
        }
        catch ( CloneNotSupportedException ignore ) {
            return null;
        }
    }

    @Override
    public void add( QueueElement queueElement ) {
        this.mDPQueueManipulator.add( queueElement, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public QueueElement peek() {
        QueueElement peek = this.mDPQueueManipulator.peek(this.mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta);
        this.mnCurrentPos = this.mDPQueueManipulator.nextPos( mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        return peek;
    }

    @Override
    public long size() {
        return this.mDPQueueManipulator.size( this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public boolean isEmpty() {
        return this.mDPQueueManipulator.size( this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta ) == 0;
    }

    @Override
    public boolean contains(QueueElement queueElement) {
        QueueElement query = this.mDPQueueManipulator.query(queueElement.getEnumId(), this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta);
        return !(query == null);
    }

    @Override
    public QueueElement remove(long enumId) {
        return this.mDPQueueManipulator.remove( this.mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }
}
