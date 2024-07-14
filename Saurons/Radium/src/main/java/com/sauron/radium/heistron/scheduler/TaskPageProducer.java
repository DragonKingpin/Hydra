package com.sauron.radium.heistron.scheduler;

import com.pinecone.slime.chunk.scheduler.ActivePageScheduler;

public interface TaskPageProducer extends ActivePageScheduler, TaskProducer {
    TaskPage require();
}
