package com.pinecone.hydra.unit.pqueue;

import com.pinecone.hydra.unit.pqueue.entity.QueueElement;

public interface MegaDeflectPriorityQueue extends MegaPriorityQueue, SharedSegmentQueue {
    void add( QueueElement queueElement );

    QueueElement peek();

    long size();

    boolean isEmpty();

    boolean contains( QueueElement queueElement );

    void setCurrentPos( long mnCurrentPos );

    void reset();

    QueueElement remove( long enumId );


}
