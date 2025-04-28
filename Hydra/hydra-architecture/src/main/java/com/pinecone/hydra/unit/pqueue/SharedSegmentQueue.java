package com.pinecone.hydra.unit.pqueue;

import com.pinecone.framework.system.prototype.Pinenut;

public interface SharedSegmentQueue extends Pinenut {

    String getSharedSegmentField();

    String getSharedSegmentName();

}
