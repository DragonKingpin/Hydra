package com.pinecone.hydra.umb.broadcast.proxy;

import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umct.husky.compiler.MethodPrototype;
import com.pinecone.hydra.umct.proxy.UMCTHub;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.DynamicMethodPrototype;
import com.pinecone.hydra.umct.stereotype.IfaceUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class GenericIfaceProxyFactory implements IfaceProxyFactory {
    protected final ConcurrentHashMap<Class<?>, Enhancer> mEnhancerCache = new ConcurrentHashMap<>();

    protected BroadcastControlProducer  mProducer;

    public GenericIfaceProxyFactory( BroadcastControlProducer producer ) {
        this.mProducer = producer;
    }

    @Override
    public <T> T createProxy( BroadcastControlProducer producer, ClassDigest classDigest, Class<T> iface, String topic, String ns, String name ) {
//        if (!iface.isInterface()) {
//            throw new IllegalArgumentException("The provided class must be an interface.");
//        }

        Enhancer enhancer = this.mEnhancerCache.computeIfAbsent(iface, clazz -> {
            Enhancer e = new Enhancer();
            e.setSuperclass(UMCTHub.class);
            e.setInterfaces( new Class[]{iface} );

            e.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept( Object obj, Method method, Object[] args, MethodProxy proxy ) throws Throwable {
                    String methodName = IfaceUtils.getIfaceMethodName( method );
                    MethodPrototype methodPrototype = (DynamicMethodPrototype) producer.queryMethodDigest(
                            classDigest.getClassName() + Namespace.DEFAULT_SEPARATOR + methodName
                    );

                    producer.issueInform(
                            topic, ns,
                            name,
                            methodPrototype,
                            args
                    );
                    return null;
                }
            });
            return e;
        });

        return iface.cast( enhancer.create() );
    }

    @Override
    public <T> T createProxy( BroadcastControlProducer producer, Class<T> iface, String topic, String ns, String name ) {
        ClassDigest classDigest = producer.queryClassDigest( iface.getName() );

        return this.createProxy( producer, classDigest, iface, topic, ns, name );
    }

    @Override
    public <T> T createProxy( Class<T> iface, String topic, String ns, String name ) {
        return this.createProxy( this.mProducer, iface, topic, ns, name );
    }

}
