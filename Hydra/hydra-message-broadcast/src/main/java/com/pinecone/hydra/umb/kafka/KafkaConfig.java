package com.pinecone.hydra.umb.kafka;

import java.util.Map;

public class KafkaConfig implements KConfig {
    protected String mszServer;

    protected String mszAutoOffsetReset;

    protected long   mnDefaultPollHandleMillis;

    public KafkaConfig ( Map<String, Object > conf ) {
        this.mszServer                 = (String) conf.get( "server" );
        this.mszAutoOffsetReset        = (String) conf.getOrDefault( "AutoOffsetReset", KafkaConstants.DefaultAutoOffsetReset );
        this.mnDefaultPollHandleMillis = ( (Number)conf.getOrDefault( "DefaultPollHandleMillis", KafkaConstants.DefaultPollHandleMillis ) ).longValue();
    }

    public KafkaConfig( String szServer, String szAutoOffsetReset, long nDefaultPollHandleMillis ){
        this.mszServer                 = szServer;
        this.mszAutoOffsetReset        = szAutoOffsetReset;
        this.mnDefaultPollHandleMillis = nDefaultPollHandleMillis;
    }

    public KafkaConfig( String szServer ){
        this( szServer, KafkaConstants.DefaultAutoOffsetReset, KafkaConstants.DefaultPollHandleMillis );
    }

    @Override
    public String getMszServer() {
        return this.mszServer;
    }

    @Override
    public String getMszAutoOffsetReset() {
        return this.mszAutoOffsetReset;
    }

    @Override
    public long getMnDefaultPollHandleMillis() {
        return this.mnDefaultPollHandleMillis;
    }
}
