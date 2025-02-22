package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.UMCHead;

public interface ExtraHeadEncoder extends Pinenut {
    byte[] encode( UMCHead head, Object jo );
}
