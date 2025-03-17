package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.UMBClientException;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastNode;
import com.pinecone.hydra.umb.broadcast.UNT;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class UlfBroadcastProducer<K, V > implements KBroadcastProducer<K, V > {
    protected static Properties newDefaultProperties( KConfig kafkaConfig ) {
        Properties properties = new Properties();

        properties.put( "bootstrap.servers", kafkaConfig.getMszServer() );
        properties.put( "key.serializer", StringSerializer.class.getName() );
        properties.put( "value.serializer", ByteArraySerializer.class.getName() );

        return properties;
    }

    protected String                       server;

    protected KClient                      kafkaClient;

    protected KafkaProducer<K, V>          kafkaProducer;

    protected Properties                   properties;

    public UlfBroadcastProducer( KClient kafkaClient, Properties properties ){
        this.kafkaClient            = kafkaClient;
        KConfig kafkaConfig         = kafkaClient.getKafkaConfig();
        this.server                 = kafkaConfig.getMszServer();
        this.properties             = properties;
    }

    public UlfBroadcastProducer( KClient kafkaClient ){
        this( kafkaClient, UlfBroadcastProducer.newDefaultProperties( kafkaClient.getKafkaConfig() ) );
    }

    public KClient getKafkaClient(){
        return this.kafkaClient;
    }

    @Override
    public void close() {
        if ( this.kafkaProducer != null ) {
            this.kafkaProducer.close();
            this.kafkaClient.deregister( this );
            this.kafkaProducer = null;
        }
    }

    @Override
    public void start() throws UMBServiceException {
        this.close();
        this.kafkaProducer = new KafkaProducer<>( this.properties );
    }

    @Override
    public void sendPrototypeMessage( String topic, String ns, K name, V body ) throws UMBClientException {
        ProducerRecord<K, V > producerRecord = new ProducerRecord<>( topic, name, body );
        this.kafkaProducer.send( producerRecord );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void sendMessage( String topic, String ns, String name, byte[] body ) throws UMBClientException {
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>( topic, name, body );
        this.kafkaProducer.send( (ProducerRecord<K, V >) producerRecord );
    }

    @Override
    public void sendMessage( String topic, byte[] body ) throws UMBClientException {
        this.sendMessage(topic, "", BroadcastNode.DefaultEntityName,body);
    }

    @Override
    public void sendMessage( UNT unt, String name, byte[] body ) throws UMBClientException {
        this.sendMessage( unt.getTopic(), unt.getNamespace(), name, body );
    }
}
