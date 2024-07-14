package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

public interface MsgProtocol extends Pinenut {
    Medium getMessageSource();
}
