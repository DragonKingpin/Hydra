package com.pinecone.hydra.uma.proxy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;

public interface PassiveClientIfaceProxyFactory extends Pinenut {
    <T> T createProxy( long clientId, DuplexAppointServer server, ClassDigest classDigest, Class<T> iface ) ;

    <T> T createProxy( long clientId, DuplexAppointServer server, Class<T> iface ) ;

    <T> T createProxy( long clientId, Class<T> iface );
}
