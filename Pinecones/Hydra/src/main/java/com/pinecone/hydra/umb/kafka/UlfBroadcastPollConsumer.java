package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.UlfPackageMessageHandler;
import com.pinecone.hydra.umb.broadcast.PollResult;
import com.pinecone.hydra.umb.broadcast.converter.GenericResultBytesConverter;
import com.pinecone.hydra.umb.broadcast.converter.ResultBytesConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class UlfBroadcastPollConsumer<K, V > implements KBroadcastPollConsumer<K, V > {
    protected static Properties newDefaultProperties( KConfig kafkaConfig, String group ) {
        Properties properties = new Properties();

        properties.put( "bootstrap.servers", kafkaConfig.getServer() );
        properties.put( "group.id", group );
        properties.put( "key.deserializer", StringDeserializer.class.getName() );
        properties.put( "value.deserializer", ByteArrayDeserializer.class.getName() );
        properties.put( "auto.offset.reset", kafkaConfig.getAutoOffsetReset() );

        return properties;
    }

    protected KClient       kafkaClient;

    protected Properties    properties;

    protected String        topic;

    protected String        group;

    protected KafkaConsumer<K, V > wrappedConsumer;

    protected AtomicBoolean pollConsumerCloseSignal;

    protected ResultBytesConverter<V > resultBytesConverter;

    public UlfBroadcastPollConsumer( KClient kafkaClient, String topic, String group, Properties properties, ResultBytesConverter<V > resultBytesConverter ){
        this.kafkaClient              = kafkaClient;
        this.properties               = properties;
        this.topic                    = topic;
        this.group                    = group;
        this.pollConsumerCloseSignal  = new AtomicBoolean( false );
        this.resultBytesConverter     = resultBytesConverter;
    }

    @SuppressWarnings( "unchecked" )
    public UlfBroadcastPollConsumer( KClient kafkaClient, String topic, String group, Properties properties ){
        this( kafkaClient, topic, group, properties, (ResultBytesConverter<V >) kafkaClient.getDafaultResultBytesConverter() );
    }

    public UlfBroadcastPollConsumer( KClient kafkaClient, String topic, String group ){
        this(
                kafkaClient, topic, group,
                UlfBroadcastPollConsumer.newDefaultProperties( kafkaClient.getKafkaConfig(), group )
        );
    }
    @Override
    public void close() {
        if ( this.wrappedConsumer != null ) {
            this.wrappedConsumer.close();
            this.kafkaClient.deregister( this );
            this.wrappedConsumer = null;
        }
    }

    @Override
    public void start( UlfPackageMessageHandler handler ) throws UMBServiceException {
        try {
            this.close();
            this.wrappedConsumer = this.newBytesConsumer( handler );
        }
        catch ( Exception e ) {
            throw new UMBServiceException( e );
        }
    }


    @Override
    public List<PollResult > startPull(long mils ) {
        this.close();

        KafkaConsumer<K, V > kafkaConsumer = new KafkaConsumer<>( this.properties );
        kafkaConsumer.subscribe(Collections.singletonList( this.topic ) );

        ConsumerRecords<K, V > records = kafkaConsumer.poll( Duration.ofMillis( mils ) );

        ArrayList<PollResult> pollResults = new ArrayList<>();
        for ( ConsumerRecord<K, V > record : records ) {
            KafkaPollResult kafkaPollResult = new KafkaPollResult(
                    record.key(), record.value(),
                    this.resultBytesConverter.convert(record.value()), new Object[] {record.headers(),record.topic(),record.offset()}
            );
            pollResults.add(kafkaPollResult);
        }
        return pollResults;
    }

    protected KafkaConsumer<K, V > newBytesConsumer( UlfPackageMessageHandler handler ) throws Exception {
        KafkaConsumer<K, V > kafkaConsumer = new KafkaConsumer<>(this.properties);
        kafkaConsumer.subscribe(Collections.singletonList( this.topic ) );

        long pollMills = this.kafkaClient.getKafkaConfig().getDefaultPollHandleMillis();
        while ( true ) {
            ConsumerRecords<K, V > records = kafkaConsumer.poll( Duration.ofMillis( pollMills ) );
            for ( ConsumerRecord<K, V > record : records ) {
                handler.onSuccessfulMsgReceived(
                        this.resultBytesConverter.convert(record.value()), new Object[] {record.key(), record.headers()}
                );
            }

            if ( this.pollConsumerCloseSignal.get() ) {
                break;
            }
        }

        return kafkaConsumer;
    }

    public KClient getKafkaClient(){
        return this.kafkaClient;
    }

}
