package com.pinecone.hydra.umb.kafka;

import com.pinecone.framework.system.prototype.Pinenut;

public interface KConfig extends Pinenut {
    String getServer();

    String getAutoOffsetReset();

    long getDefaultPollHandleMillis();

}
