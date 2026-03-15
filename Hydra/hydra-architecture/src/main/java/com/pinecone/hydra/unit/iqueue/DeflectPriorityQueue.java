package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;

import java.util.List;

public interface DeflectPriorityQueue extends MegaPriorityQueue, SharedSegmentIQueue {
    void pushBack( QueueElement queueElement );

    void pushFront( QueueElement queueElement );

    QueueElement getByIndex( long index );

    QueueElement popFront();

    List<QueueElement> fetchElements( long offset, long limit );

    List<GUID> fetchElementGuids( long offset, long limit );

    QueueElement popBack();

    long size();

    boolean isEmpty();

    boolean contains( QueueElement queueElement );

    void setCurrentPos( long mnCurrentPos );

    void reset();

    QueueElement remove( long enumId );

    List<QueueElement> fetchElementByPriority( long priority, long offset, long limit );

    String getSegmentName();

    QueueMasterManipulator getMasterManipulator();
}
