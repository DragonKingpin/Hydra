package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umb.broadcast.BroadcastConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastProducer;
import com.pinecone.hydra.umb.broadcast.UNT;
import com.pinecone.hydra.umb.broadcast.converter.GenericResultBytesConverter;
import com.pinecone.hydra.umb.broadcast.converter.ResultBytesConverter;
import com.pinecone.hydra.umc.msg.Messagus;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaClient implements KClient {
    protected Map<BroadcastProducer, Object> producerRegister;

    protected Map<BroadcastConsumer, Object> consumerRegister;

    private static final Object PRESENT = new Object();

    protected KConfig       kafkaConfig;

    protected long          nodeId;

    protected ExecutorService pollConsumerThreadPool;

    protected ResultBytesConverter<Object > resultBytesConverter;

    public KafkaClient( long nodeId, KConfig config ) {
        this.kafkaConfig             = config;

        this.producerRegister        = new ConcurrentHashMap<>();
        this.consumerRegister        = new ConcurrentHashMap<>();
        this.nodeId                  = nodeId;
        this.resultBytesConverter    = new GenericResultBytesConverter<>();
        this.pollConsumerThreadPool  = Executors.newCachedThreadPool();
    }

    public KafkaClient( long nodeId, String server ) {
        this( nodeId, new KafkaConfig( server ) );
    }

    public KafkaClient( String server ) {
        this( Messagus.nextLocalId(), server );
    }

    public KafkaClient( Map<String, Object> config ){
        this( Messagus.nextLocalId(), new KafkaConfig( config ) );
    }

    @Override
    public void close() {
        for( Map.Entry<BroadcastConsumer, Object> kv : this.consumerRegister.entrySet() ) {
            kv.getKey().close();
        }

        for( Map.Entry<BroadcastProducer, Object> kv : this.producerRegister.entrySet() ) {
            kv.getKey().close();
        }

        this.consumerRegister.clear();
        this.producerRegister.clear();

        this.pollConsumerThreadPool.shutdown();
    }

    @Override
    public void register( BroadcastProducer producer ) {
        this.producerRegister.put( producer, PRESENT );
    }

    @Override
    public void register( BroadcastConsumer consumer ) {
        this.consumerRegister.put( consumer, PRESENT );
    }

    @Override
    public void deregister( BroadcastProducer producer ) {
        this.producerRegister.remove( producer );
    }

    @Override
    public void deregister( BroadcastConsumer consumer ) {
        this.consumerRegister.remove( consumer );
    }

    @Override
    public BroadcastProducer createProducer() {
        UlfBroadcastProducer<String, byte[] > ulfBroadcastProducer = new UlfBroadcastProducer<>(this);
        this.register(ulfBroadcastProducer);
        return ulfBroadcastProducer;
    }

    @Override
    public BroadcastConsumer createConsumer( String topic, String ns ) {
        UlfBroadcastPollConsumer<String, byte[] > kafkaBroadcastConsumer = new UlfBroadcastPollConsumer<>(this, topic, ns);
        this.register(kafkaBroadcastConsumer);
        return kafkaBroadcastConsumer;
    }

    @Override
    public <K, V > KBroadcastProducer<K, V > createPrototypeProducer( Properties properties ) {
        UlfBroadcastProducer<K, V > ulfBroadcastProducer = new UlfBroadcastProducer<> ( this, properties );
        this.register(ulfBroadcastProducer);
        return ulfBroadcastProducer;
    }

    @Override
    public <K, V > KBroadcastPollConsumer<K, V >  createPrototypeConsumer( String topic, String ns, Properties properties ) {
        UlfBroadcastPollConsumer<K, V >  kafkaBroadcastConsumer = new UlfBroadcastPollConsumer<>( this, topic, ns, properties );
        this.register(kafkaBroadcastConsumer);
        return kafkaBroadcastConsumer;
    }

    @Override
    public BroadcastConsumer createConsumer( String topic ) {
        return this.createConsumer(topic, "");
    }

    @Override
    public BroadcastConsumer createConsumer( UNT unt ) {
        return this.createConsumer( unt.getTopic(), unt.getNamespace() );
    }

    @Override
    public KConfig getKafkaConfig() {
        return this.kafkaConfig;
    }

    @Override
    public KConfig getMessageNodeConfig() {
        return this.getKafkaConfig();
    }

    @Override
    public ResultBytesConverter<Object > getDafaultResultBytesConverter() {
        return this.resultBytesConverter;
    }

    @Override
    public long getMessageNodeId() {
        return this.nodeId;
    }

    @Override
    public ExtraHeadCoder getExtraHeadCoder() {
        return null;
    }

    protected ExecutorService getPollConsumerThreadPool() {
        return this.pollConsumerThreadPool;
    }


    @Override
    public Collection<BroadcastConsumer> viewConsumerRegister() {
        return Collections.unmodifiableSet(this.consumerRegister.keySet());
    }

    @Override
    public Collection<BroadcastProducer> viewProducerRegister() {
        return Collections.unmodifiableSet(this.producerRegister.keySet());
    }

}
