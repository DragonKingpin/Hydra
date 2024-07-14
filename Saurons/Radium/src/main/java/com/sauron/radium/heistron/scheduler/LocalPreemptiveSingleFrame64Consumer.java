package com.sauron.radium.heistron.scheduler;

public class LocalPreemptiveSingleFrame64Consumer implements TaskFrame64Consumer {
    protected TaskFrame64Producer            mFrameProducer;
    protected PageFrame64ConsumerAdapter     mFrame64ConsumerAdapter;

    public LocalPreemptiveSingleFrame64Consumer( TaskFrame64Producer pageProducer, PageFrame64ConsumerAdapter frame64ConsumerAdapter ) {
        this.mFrameProducer           = pageProducer;
        this.mFrame64ConsumerAdapter  = frame64ConsumerAdapter;
    }

    @Override
    public void consume () {
        Long id = this.mFrameProducer.require();

        while ( id != null ) {
            if( this.mFrameProducer.hasTerminateSignal() || Thread.currentThread().isInterrupted() ) {
                break;
            }

            try{
                this.mFrame64ConsumerAdapter.consumeById( id );

                id = this.mFrameProducer.require();
            }
            finally {
                this.mFrameProducer.deactivate( id );
            }
        }
    }

    @Override
    public TaskFrame64Producer getTaskPageProducer() {
        return this.mFrameProducer;
    }
}
