package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.UMCBroadcastNode;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastProducer;

public interface UlfKafkaClient extends KClient, UMCBroadcastNode {
    UMCBroadcastProducer createUlfProducer() ;
}
