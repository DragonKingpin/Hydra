package com.pinecone.hydra.unit.iqueue;

import com.pinecone.hydra.unit.iqueue.entity.QueueElement;

public interface MegaDeflectPriorityQueue extends MegaPriorityQueue, SharedSegmentIQueue {
    void pushBack( QueueElement queueElement );

    void pushFront( QueueElement queueElement );

    QueueElement getByIndex( long index );

    QueueElement popFront();

    QueueElement popBack();

    long size();

    boolean isEmpty();

    boolean contains( QueueElement queueElement );

    void setCurrentPos( long mnCurrentPos );

    void reset();

    QueueElement remove( long enumId );


}
