package com.pinecone.hydra.umb.broadcast;

import java.io.IOException;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umct.husky.compiler.MethodPrototype;

public interface BroadcastControlProducer extends BroadcastControlAgent {

    void issueInform( UNT unt, String name, MethodPrototype method, Object[] args ) throws IOException ;

    void issueInform( String topic, String ns, String name, MethodPrototype method, Object[] args ) throws IOException ;

    void issueInform( String topic, MethodPrototype method, Object[] args ) throws IOException ;

    void issueInform( String topic, String szMethodAddress, Object... args ) throws IOException ;

    <T> T getIface( Class<T> iface, String topic, String ns, String name );

    default <T> T getIface( Class<T> iface, String topic ){
        return this.getIface( iface, topic, "", BroadcastNode.DefaultEntityName );
    }

    void close();

    void start() throws UMBServiceException;

}
