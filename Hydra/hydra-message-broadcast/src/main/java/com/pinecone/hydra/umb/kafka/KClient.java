package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.BroadcastConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastNode;
import com.pinecone.hydra.umb.broadcast.BroadcastProducer;
import com.pinecone.hydra.umb.broadcast.converter.ResultBytesConverter;
import com.pinecone.hydra.umc.msg.handler.ErrorMessageAudit;

import java.util.Collection;
import java.util.Properties;

public interface KClient extends BroadcastNode {
    @Override
    default ErrorMessageAudit    getErrorMessageAudit() {
        return null;
    }

    @Override
    default void                 setErrorMessageAudit( ErrorMessageAudit audit ){

    }

    KConfig getKafkaConfig();

    <K, V > KBroadcastProducer<K, V > createPrototypeProducer( Properties properties ) ;

    default KBroadcastProducer<String, byte[] > createProducer( Properties properties ) {
        return this.createPrototypeProducer( properties );
    }

    <K, V > KBroadcastPollConsumer<K, V > createPrototypeConsumer( String topic, String ns, Properties properties ) ;

    default KBroadcastPollConsumer<String, byte[] > createConsumer( String topic, String ns, Properties properties ) {
        return this.createPrototypeConsumer( topic, ns, properties );
    }

    ResultBytesConverter<Object > getDafaultResultBytesConverter();


    Collection<BroadcastProducer> viewProducerRegister();

    Collection<BroadcastConsumer> viewConsumerRegister();
}
