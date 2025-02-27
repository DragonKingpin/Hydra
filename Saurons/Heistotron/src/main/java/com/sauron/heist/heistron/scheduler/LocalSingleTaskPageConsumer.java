package com.sauron.heist.heistron.scheduler;

public class LocalSingleTaskPageConsumer extends SingleTaskPageConsumer {
    public LocalSingleTaskPageConsumer( TaskPageProducer pageProducer, PageFrame64ConsumerAdapter frame64ConsumerAdapter ) {
        super( pageProducer, frame64ConsumerAdapter );
    }
}
