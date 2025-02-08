package com.sauron.heist.heistron.scheduler;

import com.pinecone.radium.system.Saunut;

public interface TaskConsumer extends Saunut {
    void consume();
}
