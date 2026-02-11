package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.UMCBroadcastConsumer;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umc.msg.Messagus;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.msg.extra.GenericExtraHeadCoder;
import com.pinecone.hydra.umc.msg.handler.ErrorMessageAudit;
import com.pinecone.hydra.umc.msg.handler.GenericErrorMessageAudit;

import java.util.Map;

public class WolfMCKafkaClient extends KafkaClient implements UlfKafkaClient{
    protected ExtraHeadCoder mExtraHeadCoder;

    protected ErrorMessageAudit mErrorMessageAudit;

    public WolfMCKafkaClient( long nodeId, KafkaConfig config, ExtraHeadCoder extraHeadCoder ) {
        super( nodeId, config );

        this.mExtraHeadCoder           = extraHeadCoder;
        this.mErrorMessageAudit        = new GenericErrorMessageAudit( this );
    }

    public WolfMCKafkaClient( long nodeId, String nameSrvAddr, ExtraHeadCoder extraHeadCoder ) {
        this( nodeId, new KafkaConfig( nameSrvAddr ), extraHeadCoder );
    }

    public WolfMCKafkaClient( String nameSrvAddr ) {
        this( Messagus.nextLocalId(), nameSrvAddr, new GenericExtraHeadCoder() );
    }

    public WolfMCKafkaClient( long nodeId, Map<String, Object> config, ExtraHeadCoder extraHeadCoder ){
        this( nodeId, new KafkaConfig( config ), extraHeadCoder );
    }

    public WolfMCKafkaClient( Map<String, Object> config, ExtraHeadCoder extraHeadCoder ){
        this( Messagus.nextLocalId(), config, extraHeadCoder );
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
