package com.sauron.radium.heistron.scheduler;

import com.pinecone.framework.system.prototype.Pinenut;
import com.sauron.radium.heistron.Heistium;
import com.pinecone.slime.chunk.RangedPage;
import com.pinecone.slime.chunk.scheduler.PageDivider;
import com.pinecone.slime.chunk.scheduler.PagePool;
import com.pinecone.slime.chunk.scheduler.PageRecycleStrategy;

public interface TaskSchedulerStrategy extends Pinenut {
    Heistium getParentHeistium();

    RangedPage getMasterPage();

    PagePool getHeistTaskPagePool();

    PageDivider getPageDivider();

    PageRecycleStrategy getPageRecycleStrategy();

    TaskSchedulerStrategy setHeistTaskPagePool( PagePool pagePool );

    TaskSchedulerStrategy setPageDivider( PageDivider divider );

    TaskSchedulerStrategy setPageRecycleStrategy( PageRecycleStrategy strategy );

    TaskProducer formulateProducer();
}
