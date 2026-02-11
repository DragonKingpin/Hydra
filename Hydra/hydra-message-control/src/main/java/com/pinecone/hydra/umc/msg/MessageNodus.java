package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.msg.handler.ErrorMessageAudit;

public interface MessageNodus extends Messagus {

    ExtraHeadCoder       getExtraHeadCoder();

    ErrorMessageAudit    getErrorMessageAudit();

    void                 setErrorMessageAudit( ErrorMessageAudit audit );

    MsgNodeConfig        getMessageNodeConfig();
}
