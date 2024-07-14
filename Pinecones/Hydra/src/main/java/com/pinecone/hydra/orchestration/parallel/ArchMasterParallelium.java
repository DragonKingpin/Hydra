package com.pinecone.hydra.orchestration.parallel;

import com.pinecone.framework.system.executum.ArchThreadum;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.orchestration.ArchExertion;
import com.pinecone.hydra.orchestration.GraphNode;
import com.pinecone.hydra.orchestration.Parallel;
import com.pinecone.hydra.orchestration.UnfulfilledActionException;

public abstract class ArchMasterParallelium extends ArchExertion implements ParallelExertion {
    protected boolean       mbIsDetachedParallelium     = true;

    protected long          mnMaximumExecutionMillis    = -1;

    protected boolean       mbIsForceSynchronized       = false;

    protected final Object  mFinaleLock                 = new Object();


    protected Processum    mParentProcessum             = null;

    protected Executum     mMasterExecutum              = new ArchThreadum( null, this.mParentProcessum ) {
        @Override
        public void apoptosis() {
            super.apoptosis(); //TODO
        }
    };

    protected Thread       mMasterStartWrapThread       = new Thread( new Runnable() {
        protected ArchMasterParallelium ium = ArchMasterParallelium.this;

        @Override
        public void run() {
            this.ium.invokeMasterSeqStart();
        }
    });

    protected Thread       mMasterRollbackWrapThread    = new Thread( new Runnable() {
        protected ArchMasterParallelium ium = ArchMasterParallelium.this;

        @Override
        public void run() {
            this.ium.invokeMasterSeqRollback();
        }
    });


    public ArchMasterParallelium() {

    }


    protected void invokeMasterSeqStart(){
        try{
            super.start();
        }
        finally {
            this.after_master_thread_finished();
        }
    }

    protected void invokeMasterSeqRollback(){
        try{
            super.rollback();
        }
        finally {
            this.after_master_thread_finished();
        }
    }


    protected void before_master_thread_executing() {
        GraphNode parent = this.parent();
        if( parent instanceof Parallel) {
            ((Parallel) parent).notifyExecuting( this );
        }
    }

    protected void join_master_thread_if_is() {
        if( this.isJoined() ) {
            try{
                if( this.mnMaximumExecutionMillis <= 0 ) {
                    this.getMasterExecutum().getAffiliateThread().join();
                }
                else {
                    this.getMasterExecutum().getAffiliateThread().join( this.mnMaximumExecutionMillis );
                }
            }
            catch ( InterruptedException e ) {
                throw new UnfulfilledActionException( e );
            }
        }
    }

    protected void after_master_thread_finished() {
        this.releaseFinaleLock();
        GraphNode parent = this.parent();
        if( parent instanceof Parallel) {
            ((Parallel) parent).notifyFinished( this );
        }
    }

    @Override
    public Executum getMasterExecutum(){
        return this.mMasterExecutum;
    }

    @Override
    public void releaseFinaleLock(){
        if( this.isForceSynchronized() ) {
            synchronized ( this.mFinaleLock ) {
                this.mFinaleLock.notify();
            }
        }
    }

    @Override
    public void start() {
        this.before_master_thread_executing();
        this.mMasterExecutum.setThreadAffinity( this.mMasterStartWrapThread );
        this.mMasterExecutum.getAffiliateThread().start();

        this.join_master_thread_if_is();
    }

    @Override
    public boolean isForceSynchronized() {
        return this.mbIsForceSynchronized;
    }

    @Override
    public void terminate() {
        super.terminate();
        this.mMasterExecutum.kill();
    }

    @Override
    public void rollback() {
        this.before_master_thread_executing();
        this.mMasterExecutum.setThreadAffinity( this.mMasterRollbackWrapThread );
        this.mMasterExecutum.getAffiliateThread().start();
    }

    @Override
    public Object getFinaleLock(){
        return this.mFinaleLock;
    }

    @Override
    public ParallelExertion setForceSynchronized() {
        this.mbIsForceSynchronized = true;
        return this;
    }

    @Override
    public ParallelExertion setNoneSynchronized() {
        this.mbIsForceSynchronized = false;
        return this;
    }

    @Override
    public boolean isDetached() {
        return this.mbIsDetachedParallelium;
    }

    @Override
    public boolean isJoined() {
        return !this.mbIsDetachedParallelium;
    }

    @Override
    public ParallelExertion setDetach() {
        this.mbIsDetachedParallelium = true;
        return this;
    }

    @Override
    public ParallelExertion setJoin() {
        this.mbIsDetachedParallelium = false;
        return this;
    }

    @Override
    public ParallelExertion setMaximumExecutionTime( long millis ) {
        this.mnMaximumExecutionMillis = millis;
        return this;
    }

    @Override
    public long getMaximumExecutionTime() {
        return this.mnMaximumExecutionMillis;
    }

    @Override
    public String nomenclature( Thread that ) {
        return String.format( "action-%s-%s", this.getName(), that.getName() ).toLowerCase();
    }
}
