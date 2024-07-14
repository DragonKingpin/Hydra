package com.sauron.radium.heistron.scheduler;

public interface TaskPageConsumer extends TaskConsumer {
    TaskPageProducer getTaskPageProducer();
}
