package com.pinecone.hydra.umb.kafka;

import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.MessageNodus;

import java.io.InputStream;
import java.io.OutputStream;

public class KafkaMedium implements Medium {
    protected MessageNodus mMessageNode;

    public KafkaMedium( MessageNodus medium ){
        this.mMessageNode = medium;
    }

    @Override
    public Object getNativeMessageSource() {
        throw new NotImplementedException();
    }

    @Override
    public OutputStream getOutputStream() {
        throw new NotImplementedException();
    }

    @Override
    public InputStream getInputStream() {
        throw new NotImplementedException();
    }

    @Override
    public String sourceName() {
        return "Kafka";
    }

    @Override
    public void release() {

    }

    @Override
    public MessageNodus getMessageNode() {
        return this.mMessageNode;
    }
}
