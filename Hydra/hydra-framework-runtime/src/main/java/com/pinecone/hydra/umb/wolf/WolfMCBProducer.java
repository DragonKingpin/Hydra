package com.pinecone.hydra.umb.wolf;

import java.io.IOException;

import com.pinecone.hydra.umb.broadcast.proxy.GenericIfaceProxyFactory;
import com.pinecone.hydra.umb.broadcast.proxy.IfaceProxyFactory;

import com.google.protobuf.DynamicMessage;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.UlfMBInformMessage;
import com.pinecone.hydra.umb.broadcast.BroadcastControlNode;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umct.husky.compiler.MethodPrototype;

public class WolfMCBProducer extends ArchBroadcastControlAgent implements BroadcastControlProducer {
    protected UMCBroadcastProducer          mBroadcastProducer;

    protected IfaceProxyFactory             mIfaceProxyFactory;

    public WolfMCBProducer ( BroadcastControlNode controlNode, UMCBroadcastProducer broadcastProducer ) {
        super( controlNode );

        this.mBroadcastProducer = broadcastProducer;
        this.mIfaceProxyFactory = new GenericIfaceProxyFactory( this );
    }


    @Override
    public void issueInform( UNT unt, String name, MethodPrototype method, Object[] args ) throws IOException {
        DynamicMessage message = this.reinterpretMsg( method, args );

        this.mBroadcastProducer.sendMessage( unt, name, new UlfMBInformMessage( message.toByteArray() ) );
    }

    @Override
    public void issueInform( String topic, String ns, String name, MethodPrototype method, Object[] args ) throws IOException {
        DynamicMessage message = this.reinterpretMsg( method, args );

        this.mBroadcastProducer.sendMessage( topic, ns, name, new UlfMBInformMessage( message.toByteArray() ) );
    }

    @Override
    public void issueInform( String topic, MethodPrototype method, Object[] args ) throws IOException {
        DynamicMessage message = this.reinterpretMsg( method, args );

        this.mBroadcastProducer.sendMessage( topic, new UlfMBInformMessage( message.toByteArray() ) );
    }

    @Override
    public void issueInform( String topic, String szMethodAddress, Object... args ) throws IOException {
        this.issueInform( topic, this.queryMethodPrototype( szMethodAddress ), args );
    }

    @Override
    public <T> T getIface( Class<T> iface, String topic, String ns, String name ) {
        return this.mIfaceProxyFactory.createProxy( iface, topic, ns, name );
    }

    @Override
    public void close() {
        this.mBroadcastProducer.close();
    }

    @Override
    public void start() throws UMBServiceException {
        this.mBroadcastProducer.start();
    }

}
