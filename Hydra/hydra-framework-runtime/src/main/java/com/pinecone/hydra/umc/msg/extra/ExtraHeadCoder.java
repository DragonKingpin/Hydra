package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ExtraEncode;

import java.util.Map;

public interface ExtraHeadCoder extends Pinenut {
    ExtraHeadEncoder       getEncoder();

    ExtraHeadDecoder       getDecoder();

    Map<String, Object >   newExtraHead();

    ExtraEncode            getDefaultEncode();

    void                   setDefaultEncode( ExtraEncode encode );
}
