package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.ArchUnidirectionalMCProtocol;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.UMCReceiver;

import java.io.IOException;

public class KafkaReceiver extends ArchUnidirectionalMCProtocol implements UMCReceiver {
    public KafkaReceiver(Medium messageSource){
        super(messageSource);
    }
    @Override
    public Object readInformMsg() throws IOException {
        return null;
    }

    @Override
    public UMCMessage readTransferMsg() throws IOException {
        return null;
    }

    @Override
    public UMCMessage readTransferMsgBytes() throws IOException {
        return null;
    }

    @Override
    public UMCMessage readMsg() throws IOException {
        return null;
    }

    @Override
    public UMCMessage readMsgBytes() throws IOException {
        return null;
    }
}
