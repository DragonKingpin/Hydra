package com.pinecone.hydra.umb.broadcast;

import com.pinecone.framework.system.prototype.Pinenut;

public interface PollResult extends Pinenut {
    Object getName();

    Object getValue();

    byte[] getBytesValue();

    Object[] getArgs();
}
