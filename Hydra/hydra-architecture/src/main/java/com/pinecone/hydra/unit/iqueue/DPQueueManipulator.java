package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;

public interface DPQueueManipulator extends Pinenut {
    void pushBack(QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta);

    void pushFront( QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    void incrementLinkedPriorities( QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement popFront( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement popBack( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    long queryQueueSize( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement remove( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement query( long enumId, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement getByIndex( long index, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    Long nextPos( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta  );

    Long getIndexPriority( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

}
