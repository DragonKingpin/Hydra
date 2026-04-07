package com.pinecone.hydra.umb.broadcast;

import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.UMCTNode;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.hydra.umct.husky.machinery.MCTContextMachinery;
import com.pinecone.hydra.umct.husky.machinery.RouteDispatcher;

public interface BroadcastControlNode extends UMCBroadcastNode, UMCTNode {

    UMCBroadcastNode getUMCBroadcastNode();



    RouteDispatcher getRouteDispatcher();

    MCTContextMachinery getMCTTransformer();

    InterfacialCompiler getInterfacialCompiler();

    ClassDigest queryClassDigest( String name );

    MethodDigest queryMethodDigest( String name );

    void addClassDigest( ClassDigest that );

    void addMethodDigest( MethodDigest that );

    ClassDigest compile( Class<? > clazz, boolean bAsIface );




    void registerInstance( String deliverName, Object instance, Class<?> iface ) ;

    void registerInstance( Object instance, Class<?> iface ) ;

    void registerController( String deliverName, Object instance, Class<?> controllerType ) ;

    void registerController( Object instance, Class<?> controllerType ) ;

    default void registerController( Object instance ) {
        this.registerController( instance, instance.getClass() );
    }



    void applyMCTContextMachinery( MCTContextMachinery mctContextMachinery ) ;

    void applyRouteDispatcher( RouteDispatcher routeDispatcher );

    UMCTExpress createUMCTExpress( String name, Class<?> expressType );




    BroadcastControlConsumer createBroadcastControlConsumer( UMCBroadcastConsumer workAgent, RouteDispatcher routeDispatcher ) ;

    BroadcastControlConsumer createBroadcastControlConsumer( UMCBroadcastConsumer workAgent ) ;

    BroadcastControlConsumer createBroadcastControlConsumer( UNT unt ) ;

    BroadcastControlConsumer createBroadcastControlConsumer( String topic, String ns ) ;

    BroadcastControlConsumer createBroadcastControlConsumer( String topic ) ;



    BroadcastControlProducer createBroadcastControlProducer( UMCBroadcastProducer workAgent ) ;

    BroadcastControlProducer createBroadcastControlProducer() ;


}
