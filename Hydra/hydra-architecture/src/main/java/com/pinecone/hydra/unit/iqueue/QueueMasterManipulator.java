package com.pinecone.hydra.unit.iqueue;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface QueueMasterManipulator extends KOIMasterManipulator {
    DPQueueManipulator              getDPQueueManipulator();

    DPStratumQueueManipulator       getDPStratumQueueManipulator();

    QueueExistManipulator           getQueueExistManipulator();
}
