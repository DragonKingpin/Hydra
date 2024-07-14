package com.sauron.radium.heistron.scheduler;

import com.sauron.radium.heistron.Heistium;
import com.pinecone.slime.chunk.RangedPage;
import com.pinecone.slime.chunk.Splitunk;
import com.pinecone.slime.chunk.scheduler.*;

public class LocalTaskSchedulerStrategy implements TaskSchedulerStrategy {
    protected Heistium mParentHeistium;
    protected PagePool mHeistTaskPagePool;
    protected PageDivider mPageDivider;

    protected PageRecycleStrategy mPageRecycleStrategy;
    protected TaskProducer         mTaskProducer;

    public LocalTaskSchedulerStrategy( Heistium heistium, PagePool pagePool, PageDivider divider, PageRecycleStrategy recycleStrategy ) {
        this.mParentHeistium        = heistium;
        this.mHeistTaskPagePool     = pagePool;
        this.mPageDivider           = divider;
        this.mPageRecycleStrategy   = recycleStrategy;
    }

    public LocalTaskSchedulerStrategy( Heistium heistium, long each ) {
        this( heistium, new DirectPagePool( RangedTaskPage.class ), null, null );
        this.mPageDivider = new FixedPageDivider64( (Splitunk) this.getMasterPage(), this.getHeistTaskPagePool(), each );
    }

    @Override
    public Heistium getParentHeistium() {
        return this.mParentHeistium;
    }

    @Override
    public RangedPage getMasterPage() {
        return this.getParentHeistium().getParentHeist().getMasterTaskPage();
    }

    @Override
    public PagePool getHeistTaskPagePool() {
        return this.mHeistTaskPagePool;
    }

    @Override
    public PageDivider getPageDivider() {
        return this.mPageDivider;
    }

    @Override
    public PageRecycleStrategy getPageRecycleStrategy() {
        return this.mPageRecycleStrategy;
    }



    @Override
    public TaskSchedulerStrategy setHeistTaskPagePool( PagePool pagePool ) {
        this.mHeistTaskPagePool = pagePool;
        return this;
    }

    @Override
    public TaskSchedulerStrategy setPageDivider( PageDivider divider ) {
        this.mPageDivider = divider;
        return this;
    }

    @Override
    public TaskSchedulerStrategy setPageRecycleStrategy( PageRecycleStrategy strategy ) {
        this.mPageRecycleStrategy = strategy;
        return this;
    }



    @Override
    public TaskProducer formulateProducer() {
        this.mTaskProducer = new LocalMultiActiveTaskPageProducer(
                this.mParentHeistium, this.getPageDivider(), this.getMasterPage().getId() + 1
        );

        return this.mTaskProducer;
    }


}
