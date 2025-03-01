package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.UMCBroadcastConsumer;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umc.msg.MessageNodus;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.msg.extra.GenericExtraHeadCoder;
import com.pinecone.hydra.umc.msg.handler.ErrorMessageAudit;
import com.pinecone.hydra.umc.msg.handler.GenericErrorMessageAudit;

import java.util.Map;

public class WolfMCKafkaClient extends KafkaClient implements UlfKafkaClient{
    protected ExtraHeadCoder mExtraHeadCoder;

    protected ErrorMessageAudit mErrorMessageAudit;

    public WolfMCKafkaClient( long nodeId, String nameSrvAddr, ExtraHeadCoder extraHeadCoder ){
        super( nodeId,nameSrvAddr );

        this.mExtraHeadCoder           = extraHeadCoder;
        this.mErrorMessageAudit        = new GenericErrorMessageAudit( this );
    }

    public WolfMCKafkaClient( String nameSrvAddr, ExtraHeadCoder extraHeadCoder ){
        this( MessageNodus.nextLocalId(), nameSrvAddr, extraHeadCoder );
    }

    public WolfMCKafkaClient( String nameSrvAddr ){
        this( MessageNodus.nextLocalId(), nameSrvAddr, new GenericExtraHeadCoder());

    }

    public WolfMCKafkaClient( Map<String, Object> config, ExtraHeadCoder extraHeadCoder ){
        super( config );
        this.mExtraHeadCoder           = extraHeadCoder;
        this.mErrorMessageAudit        = new GenericErrorMessageAudit( this );
    }

    @Override
    public ErrorMessageAudit getErrorMessageAudit() {
        return this.mErrorMessageAudit;
    }

    @Override
    public void setErrorMessageAudit( ErrorMessageAudit audit ){
        this.mErrorMessageAudit = audit;
    }
    @Override
    public ExtraHeadCoder getExtraHeadCoder() {
        return this.mExtraHeadCoder;
    }


    @Override
    public UMCBroadcastConsumer createUlfConsumer( String topic, String ns ) {
        WolfKafkaConsumer consumer = new WolfKafkaConsumer( this,topic,ns );
        this.register( consumer );
        return consumer;
    }

    @Override
    public UMCBroadcastConsumer createUlfConsumer( String topic ) {
        return this.createUlfConsumer( topic,"" );
    }

    @Override
    public UMCBroadcastConsumer createUlfConsumer( UNT unt ) {
        return this.createUlfConsumer( unt.getTopic(), unt.getNamespace() );
    }

    @Override
    public UMCBroadcastProducer createUlfProducer() {
        WolfKafkaProducer wolfKafkaProducer = new WolfKafkaProducer(this, this.mExtraHeadCoder);
        this.register( wolfKafkaProducer );
        return wolfKafkaProducer;
    }
}
