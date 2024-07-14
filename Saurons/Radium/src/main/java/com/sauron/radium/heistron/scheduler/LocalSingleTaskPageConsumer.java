package com.sauron.radium.heistron.scheduler;

public class LocalSingleTaskPageConsumer extends SingleTaskPageConsumer {
    public LocalSingleTaskPageConsumer( TaskPageProducer pageProducer, PageFrame64ConsumerAdapter frame64ConsumerAdapter ) {
        super( pageProducer, frame64ConsumerAdapter );
    }
}
