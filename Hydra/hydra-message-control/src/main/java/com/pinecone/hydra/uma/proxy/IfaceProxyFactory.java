package com.pinecone.hydra.uma.proxy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.uma.AppointClient;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;

public interface IfaceProxyFactory extends Pinenut {
    <T> T createProxy( AppointClient client, ClassDigest classDigest, Class<T> iface ) ;

    <T> T createProxy( AppointClient client, Class<T> iface ) ;

    <T> T createProxy( Class<T> iface );
}
