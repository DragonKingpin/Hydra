package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;

import java.util.List;

public interface DPQueueManipulator extends Pinenut {
    void pushBack(QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta);

    void pushFront( QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    void incrementLinkedPriorities( QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement popFront( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    List<QueueElement> batchPopFront( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta, long limit, long offset );

    QueueElement popBack( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    List<QueueElement> batchPopBack( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta, long limit, long offset );

    long queryQueueSize( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement remove( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement query( long enumId, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    List<QueueElement> fetchElementByPriority( long priority, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta,long limit, long offset );

    List<QueueElement> fetchElement( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta,long limit, long offset );

    List<GUID> fetchElementGuid( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta,long limit, long offset );

    QueueElement getByIndex( long index, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    Long nextPos( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta  );

    Long getIndexPriority( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

}
