package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueStratumElement;

public interface DPStratumQueueManipulator extends Pinenut {
    void pushBack(QueueStratumElement queueElement, String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta);

    QueueStratumElement popFront( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    void removeFront( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );

    long isEmpty( String sharedSegmentField, String sharedSegmentName, QueueMeta queueMeta );
}
