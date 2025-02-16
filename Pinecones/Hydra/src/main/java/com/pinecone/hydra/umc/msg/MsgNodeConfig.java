package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

public interface MsgNodeConfig extends Pinenut {
    long getSyncWaitingMillis();
}
