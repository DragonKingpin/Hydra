package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.UMBClientException;
import com.pinecone.hydra.umb.broadcast.BroadcastProducer;

public interface KBroadcastProducer<K, V > extends BroadcastProducer {

    void sendPrototypeMessage( String topic, String ns, K name, V body ) throws UMBClientException ;

}
