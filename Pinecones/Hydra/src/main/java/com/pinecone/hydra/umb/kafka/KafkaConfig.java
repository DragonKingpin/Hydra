package com.pinecone.hydra.umb.kafka;

import java.util.Map;

public class KafkaConfig implements KConfig {
    protected String server;

    protected String autoOffsetReset;

    protected long defaultPollHandleMillis;

    public KafkaConfig ( Map<String, Object > conf ) {
        this.server                  = (String) conf.get( "server" );
        this.autoOffsetReset         = (String) conf.getOrDefault("autoOffsetReset", "earliest");
        this.defaultPollHandleMillis = Long.parseLong(
                conf.getOrDefault("defaultPollHandleMillis", 100
        ).toString());
    }

    public KafkaConfig( String server, String autoOffsetReset, long defaultPollHandleMillis ){
        this.server                  = server;
        this.autoOffsetReset         = autoOffsetReset;
        this.defaultPollHandleMillis = defaultPollHandleMillis;
    }

    public KafkaConfig( String server ){
        this( server, "earliest", 100 );
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public String getAutoOffsetReset() {
        return this.autoOffsetReset;
    }

    @Override
    public long getDefaultPollHandleMillis() {
        return this.defaultPollHandleMillis;
    }
}
