package com.sauron.radium.heistron.scheduler;

import com.pinecone.slime.chunk.scheduler.ActivePageScheduler64;
import com.pinecone.slime.chunk.scheduler.LocalMapChunkRegister;
import com.pinecone.slime.chunk.scheduler.PageDivider;


public abstract class ActiveTaskPageProducer extends ActivePageScheduler64 implements TaskPageProducer {
    public ActiveTaskPageProducer(PageDivider divider, long autoIncrementId ) {
        super( divider, autoIncrementId );

        this.mChunkRegister = new LocalMapChunkRegister<>();
    }

    @Override
    public boolean hasMoreProducts() {
        return this.getDivider().remainAllocatable() > 0;
    }

    @Override
    public TaskPage require() {
        return (TaskPage) this.activate();
    }
}
