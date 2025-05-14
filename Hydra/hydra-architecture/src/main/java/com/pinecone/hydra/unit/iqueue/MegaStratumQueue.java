package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueStratumElement;

public interface MegaStratumQueue extends Pinenut {
    void pushBack( QueueStratumElement queueElement);

    QueueStratumElement popFront();

    boolean isEmpty();
}
