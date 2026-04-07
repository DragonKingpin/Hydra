package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.system.prototype.Pinenut;

public interface SharedSegmentIQueue extends Pinenut {

    String getSharedSegmentField();

    String getSharedSegmentName();

}
