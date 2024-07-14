package com.pinecone.hydra.umc.msg.handler;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.UMCMessage;

public interface ErrorMessageAudit extends Pinenut {
    boolean isErrorMessage( UMCMessage message );
}
