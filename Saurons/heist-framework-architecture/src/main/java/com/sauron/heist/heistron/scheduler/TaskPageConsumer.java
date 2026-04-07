package com.sauron.heist.heistron.scheduler;

public interface TaskPageConsumer extends TaskConsumer {
    TaskPageProducer getTaskPageProducer();
}
