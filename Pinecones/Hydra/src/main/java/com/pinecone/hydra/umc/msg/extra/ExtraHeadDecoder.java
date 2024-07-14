package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.UMCHead;

import java.util.Map;

public interface ExtraHeadDecoder extends Pinenut {
    Map<String, Object > decode( UMCHead head, byte[] raw );
}
