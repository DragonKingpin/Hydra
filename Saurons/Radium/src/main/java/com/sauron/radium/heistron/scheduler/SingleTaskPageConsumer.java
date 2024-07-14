package com.sauron.radium.heistron.scheduler;

import com.pinecone.slime.unitization.MinMaxRange64;

public abstract class SingleTaskPageConsumer implements TaskPageConsumer {
    protected TaskPageProducer               mPageProducer;
    protected PageFrame64ConsumerAdapter     mFrame64ConsumerAdapter;

    protected SingleTaskPageConsumer( TaskPageProducer pageProducer, PageFrame64ConsumerAdapter frame64ConsumerAdapter ) {
        this.mPageProducer           = pageProducer;
        this.mFrame64ConsumerAdapter = frame64ConsumerAdapter;
    }

    @Override
    public void consume () {
        while ( this.mPageProducer.hasMoreProducts() ) {
            if( this.mPageProducer.hasTerminateSignal() || Thread.currentThread().isInterrupted() ) {
                break;
            }

            TaskPage page = this.mPageProducer.require();

            try{
                this.consumeSinglePage( page );
            }
            finally {
                this.mPageProducer.deactivate( page );
            }
        }
    }

    protected void consumeSinglePage( TaskPage page ) {
        long min = ( (MinMaxRange64)page.getRange()).getMin();
        long max = ( (MinMaxRange64)page.getRange()).getMax();

        for ( long i = min; i < max; ++i ) {
            this.mFrame64ConsumerAdapter.consumeById( i );
        }
    }

    @Override
    public TaskPageProducer getTaskPageProducer() {
        return this.mPageProducer;
    }
}
