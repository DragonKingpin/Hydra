package com.pinecone.hydra.umc.msg.handler;

import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.UMCMessage;

public class GenericErrorMessageAudit implements ErrorMessageAudit {
    protected MessageNode mMessageNode;

    public GenericErrorMessageAudit( MessageNode node ) {
        this.mMessageNode = node;
    }

    @Override
    public boolean isErrorMessage( UMCMessage message ) {
        return message.getHead().getStatus().getValue() >= 500;
    }
}
