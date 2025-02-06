package com.pinecone.hydra.umb.broadcast.proxy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;

public interface IfaceProxyFactory extends Pinenut {
    <T> T createProxy( BroadcastControlProducer producer, ClassDigest classDigest, Class<T> iface, String topic, String ns, String name ) ;

    <T> T createProxy( BroadcastControlProducer producer, Class<T> iface, String topic, String ns, String name ) ;

    <T> T createProxy( Class<T> iface, String topic, String ns, String name );
}