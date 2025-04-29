package com.pinecone.hydra.unit.pqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.pqueue.entity.QueueElement;

public interface DPQueueManipulator extends Pinenut {
    void add(QueueElement queueElement);

    QueueElement poll();

    QueueElement peek();

    long size();

    QueueElement remove( QueueElement queueElement );

    void update( QueueElement queueElement );
}
