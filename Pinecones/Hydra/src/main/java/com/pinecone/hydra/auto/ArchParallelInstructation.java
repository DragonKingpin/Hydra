package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.ArchThreadum;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.executum.Processum;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ArchParallelInstructation extends ArchInstructation implements ParallelInstructation {

    protected volatile boolean mbEnded                ;
    protected long             mStartNano             ;
    protected long             mnMaxJoinMillis        ;
    protected Processum        mParentPro             ;

    protected Executum         mMasterExecutum    = new ArchThreadum( null, this.mParentPro ) {
        @Override
        public void apoptosis() {
            this.interrupt();
        }
    };

    protected Runnable         mMasterRun         = new Runnable() {
        protected ArchParallelInstructation ion = ArchParallelInstructation.this;

        @Override
        public void run() {
            try{
                if( Thread.currentThread().isInterrupted() ) {
                    if( this.ion instanceof Suggestation ) {
                        ((Suggestation) this.ion).setIgnoredReason( IgnoredReason.Interrupt );
                    }
                    return;
                }
                this.ion.doExecute();
            }
            catch ( Exception e ) {
                this.ion.setLastException( e );
                if( this.ion instanceof Suggestation ) {
                    if( e instanceof InterruptedException ) {
                        ((Suggestation) this.ion).setIgnoredReason( IgnoredReason.Interrupt );
                    }
                    else if( e instanceof AbortException || e instanceof ContinueException ) {
                        ((Suggestation) this.ion).setIgnoredReason( IgnoredReason.Abort );
                    }
                }
            }
            finally {
                this.ion.mbEnded = true;
            }
        }
    };

    protected Thread           mMasterThread      = new Thread( this.mMasterRun );

    protected ArchParallelInstructation( Processum parent, long nMaxJoinMillis ) {
        super();

        this.mbEnded         = false               ;
        this.mnMaxJoinMillis = nMaxJoinMillis      ;
        this.mParentPro      = parent              ;
        this.mStartNano      = System.nanoTime()   ;
    }

    protected ArchParallelInstructation( Processum parent ) {
        this( parent, -1 );
    }

    @Override
    public void terminate() {
        this.interrupt();
    }

    @Override
    public void interrupt(){
        this.mMasterExecutum.interrupt();
    }

    @Override
    public void kill(){
        this.mMasterExecutum.kill();
    }

    @Override
    public boolean isEnded() {
        return this.mbEnded;
    }

    @Override
    public long getStartNano() {
        return this.mStartNano;
    }

    protected abstract void doExecute() throws Exception;

    @Override
    public void execute() throws Exception {
        if( this.mbEnded && this.mMasterThread.getState() == Thread.State.TERMINATED ) {
            this.mMasterThread = new Thread( this.mMasterRun );
            this.mbEnded       = false;
        }

        this.mMasterThread.start();
        if( this.mnMaxJoinMillis == 0 ) {
            this.mMasterThread.join();
        }
        else if( this.mnMaxJoinMillis > 0 ) {
            this.mMasterThread.join( this.mnMaxJoinMillis );
        }
    }

    @Override
    public boolean isDetached() {
        return this.mnMaxJoinMillis < 0;
    }

    @Override
    public boolean isJoined() {
        return this.mnMaxJoinMillis >= 0;
    }

    @Override
    public ArchParallelInstructation setDetach() {
        this.mnMaxJoinMillis = -1;
        return this;
    }

    @Override
    public ArchParallelInstructation setJoin() {
        this.mnMaxJoinMillis = 0;
        return this;
    }

    @Override
    public long getMaxJoinMillis() {
        return this.mnMaxJoinMillis;
    }

    @Override
    public ParallelInstructation setMaxJoinMillis( long join ) {
        this.mnMaxJoinMillis = join;
        return this;
    }

    @Override
    public Thread getMasterThread() {
        return this.mMasterThread;
    }

    @Override
    public Executum tryGetMasterExecutum() {
        return this.mMasterExecutum;
    }
}