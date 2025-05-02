package com.pinecone.hydra.unit.iqueue;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;

public class MagnitudeDPQueue implements MegaDeflectPriorityQueue, Cloneable {
    private QueueMasterManipulator      mQueueMasterManipulator;

    private DPQueueManipulator          mDPQueueManipulator;

    private long                        mnCurrentPos;

    private String                      mszSharedSegmentField;

    private String                      mszSharedSegmentName;

    private QueueMeta                   mQueueMeta;

    public MagnitudeDPQueue( KOIMappingDriver driver, long currentPos,
                            String shareSegmentField, String sharedSegmentName, QueueMeta queueMeta ) {
        this.mQueueMasterManipulator    = (QueueMasterManipulator) driver.getMasterManipulator();
        this.mDPQueueManipulator        = this.mQueueMasterManipulator.getDPQueueManipulator();
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
    public void pushBack( QueueElement queueElement ) {
        this.mDPQueueManipulator.pushBack( queueElement, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public void pushFront(QueueElement queueElement) {
        this.mDPQueueManipulator.pushFront( queueElement, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        this.mDPQueueManipulator.incrementLinkedPriorities( queueElement, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public QueueElement getByIndex(long index) {
        return this.mDPQueueManipulator.getByIndex( index, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public QueueElement popFront() {
        QueueElement peek = this.mDPQueueManipulator.popFront( this.mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        Long indexPriority = this.mDPQueueManipulator.getIndexPriority( this.mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        peek.setIndexPriority( indexPriority );
        Long l = this.mDPQueueManipulator.nextPos( this.mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        if( l == null ) {
            this.mnCurrentPos = -1;
        }
        else {
            this.mnCurrentPos = l;
        }
        return peek;
    }

    @Override
    public QueueElement popBack() {
        return null;
    }

    @Override
    public long size() {
        return this.mDPQueueManipulator.queryQueueSize( this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public boolean isEmpty() {
        return this.mDPQueueManipulator.queryQueueSize( this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta ) == 0;
    }

    @Override
    public boolean contains( QueueElement queueElement ) {
        QueueElement query = this.mDPQueueManipulator.query( queueElement.getEnumId(), this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        return !(query == null);
    }

    @Override
    public QueueElement remove( long enumId ) {
        return this.mDPQueueManipulator.remove( this.mnCurrentPos, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    public long currentPosition() {
        return this.mnCurrentPos;
    }
}
