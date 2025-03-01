package com.pinecone.hydra.umb.kafka;

import java.util.Map;

public class KafkaConfig implements KConfig {
    protected String mszServer;

    protected String mszAutoOffsetReset;

    protected long mnDefaultPollHandleMillis;

    public KafkaConfig ( Map<String, Object > conf ) {
        this.mszServer = (String) conf.get( "server" );
        this.mszAutoOffsetReset = (String) conf.getOrDefault("autoOffsetReset", "earliest");
        this.mnDefaultPollHandleMillis = Long.parseLong(
                conf.getOrDefault("defaultPollHandleMillis", 100
        ).toString());
    }

    public KafkaConfig(String mszServer, String mszAutoOffsetReset, long mnDefaultPollHandleMillis){
        this.mszServer = mszServer;
        this.mszAutoOffsetReset = mszAutoOffsetReset;
        this.mnDefaultPollHandleMillis = mnDefaultPollHandleMillis;
    }

    public KafkaConfig( String mszServer){
        this(mszServer, "earliest", 100 );
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
