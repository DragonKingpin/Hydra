package com.pinecone.hydra.umb.kafka;

import com.pinecone.framework.system.Nullable;
import com.pinecone.hydra.umb.UMBClientException;
import com.pinecone.hydra.umb.UMCPackageMessageEncoder;
import com.pinecone.hydra.umb.UlfPackageMessageEncoder;
import com.pinecone.hydra.umb.broadcast.BroadcastNode;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;

import java.io.IOException;

public class WolfKafkaProducer extends UlfBroadcastProducer<String, byte[] > implements UMCBroadcastProducer {
    protected UMCPackageMessageEncoder mUMCPackageMessageEncoder;

    protected ExtraHeadCoder           mExtraHeadCoder;

    public WolfKafkaProducer( UlfKafkaClient client, @Nullable ExtraHeadCoder extraHeadCoder ){
        super( client );

        this.mExtraHeadCoder           = extraHeadCoder;
        if ( this.mExtraHeadCoder == null ) {
            this.mExtraHeadCoder = client.getExtraHeadCoder();
        }

        this.mUMCPackageMessageEncoder = new UlfPackageMessageEncoder( this.mExtraHeadCoder );
    }

    public WolfKafkaProducer( UlfKafkaClient client ){
        this(client,null);
    }

    @Override
    public UlfKafkaClient getKafkaClient() {
        return (UlfKafkaClient) this.kafkaClient;
    }

    @Override
    public void sendMessage( String topic, String ns, String name, UMCMessage message ) throws UMBClientException {
        try{
            this.sendMessage( topic, ns, name, this.mUMCPackageMessageEncoder.encode( message ) );
        }
        catch ( IOException e ) {
            throw new UMBClientException( e );
        }
    }

    @Override
    public void sendMessage( String topic, UMCMessage message ) throws UMBClientException {
        this.sendMessage( topic, "", BroadcastNode.DefaultEntityName, message );
    }

    @Override
    public void sendMessage( UNT unt, String name, UMCMessage message ) throws UMBClientException {
        this.sendMessage( unt.getTopic(), unt.getNamespace(), name, message );
    }


}
