package com.pinecone.hydra.umb.broadcast;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.UMBClientException;
import com.pinecone.hydra.umb.UMBServiceException;

public interface BroadcastProducer extends Pinenut {
    void close();

    void start() throws UMBServiceException;

    void sendMessage( String topic, String ns, String name, byte[] body ) throws UMBClientException ;

    void sendMessage( String topic, byte[] body ) throws UMBClientException ;

    void sendMessage( UNT unt, String name, byte[] body ) throws UMBClientException ;
}
