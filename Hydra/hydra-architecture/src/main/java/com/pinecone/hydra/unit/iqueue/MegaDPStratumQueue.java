package com.pinecone.hydra.unit.iqueue;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueStratumElement;

public class MegaDPStratumQueue implements MegaStratumQueue {
    private QueueMasterManipulator          mQueueMasterManipulator;

    private DPStratumQueueManipulator       mDPStratumQueueManipulator;

    private String                          mszSharedSegmentField;

    private String                          mszSharedSegmentName;

    private QueueMeta                       mQueueMeta;

    public MegaDPStratumQueue(
            KOIMappingDriver driver, String shareSegmentField,
            String sharedSegmentName, QueueMeta queueMeta
    ) {
        this.mQueueMasterManipulator = (QueueMasterManipulator) driver.getMasterManipulator();
        this.mDPStratumQueueManipulator = this.mQueueMasterManipulator.getDPStratumQueueManipulator();
        this.mszSharedSegmentName = sharedSegmentName;
        this.mszSharedSegmentField = shareSegmentField;
        this.mQueueMeta = queueMeta;
    }

    @Override
    public void pushBack(QueueStratumElement queueElement) {
        this.mDPStratumQueueManipulator.pushBack( queueElement, this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
    }

    @Override
    public QueueStratumElement popFront() {
        QueueStratumElement queueStratumElement = this.mDPStratumQueueManipulator.popFront(this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta);
        this.mDPStratumQueueManipulator.removeFront( this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta );
        return queueStratumElement;
    }

    @Override
    public boolean isEmpty() {
        return this.mDPStratumQueueManipulator.isEmpty( this.mszSharedSegmentField, this.mszSharedSegmentName, this.mQueueMeta ) == 0;
    }
}
