package com.pinecone.hydra.unit.pqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.pqueue.entity.QueueElement;

public interface DPQueueManipulator extends Pinenut {
    void add(QueueElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta);

    QueueElement peek( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    long size( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement remove( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    QueueElement query( long enumId, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    long nextPos( long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta  );
}
