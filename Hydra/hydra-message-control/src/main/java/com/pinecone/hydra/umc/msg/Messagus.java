package com.pinecone.hydra.umc.msg;

import java.util.concurrent.atomic.AtomicInteger;

import com.pinecone.framework.system.regimentation.Nodus;

public interface Messagus extends Nodus {

    AtomicInteger LocalNodeIdAllocator = new AtomicInteger( 0 );

    static int nextLocalId() {
        return MessageNodus.LocalNodeIdAllocator.getAndIncrement();
    }

    long getMessageNodeId();

}
