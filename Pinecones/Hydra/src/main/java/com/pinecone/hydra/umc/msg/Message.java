package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Message extends Pinenut {
    long getMessageLength();

    default long queryMessageLength(){
        return this.getMessageLength();
    }

    void release();
}
