package com.sauron.radium.heistron;

import com.sauron.radium.heistron.orchestration.HeistTask;
import com.sauron.radium.heistron.orchestration.Hierarchy;
import com.sauron.radium.heistron.scheduler.LocalPreemptiveTaskFrame64Producer;
import com.sauron.radium.heistron.scheduler.TaskProducer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocalHeistium extends HeistTask implements Heistium {
    protected AtomicBoolean     mTerminateSignal;
    protected int               mnMaximumThread;
    protected TaskProducer      mTaskProducer;
    protected long              mnAwaitFinishedMaxMillis;
    protected Hierarchy         mHierarchy;

    protected void initSelf( Heistum heistum, long nAwaitFinishedMaxMillis ) {
        if( heistum instanceof CascadeHeist ) {
            this.mHierarchy                  = ((CascadeHeist) heistum).getHierarchy();
        }
        else {
            this.mHierarchy                  = Hierarchy.Slave;
        }

        this.mnMaximumThread             = heistum.getMaximumThread();
        this.mTerminateSignal            = new AtomicBoolean( false );
        this.mnAwaitFinishedMaxMillis    = nAwaitFinishedMaxMillis;

        if( this.mHierarchy == Hierarchy.Slave ) {
            //TODO
            //this.mTaskProducer               = (new LocalTaskSchedulerStrategy( this, 100 )).formulateProducer();
            this.mTaskProducer               = new LocalPreemptiveTaskFrame64Producer( this, this.mParentHeist.getMasterTaskPage() );
        }
    }

    public LocalHeistium( String szName, Heistum heistum, long nAwaitFinishedMaxMillis ) {
        super( szName, heistum );
        this.initSelf( heistum, nAwaitFinishedMaxMillis );
    }

    public LocalHeistium( Heistum heistum, long nAwaitFinishedMaxMillis ) {
        super( heistum );
        this.initSelf( heistum, nAwaitFinishedMaxMillis );
    }

    @Override
    public TaskProducer getTaskProducer() {
        return this.mTaskProducer;
    }

    protected void verifyIsTerminated() {
        if( this.mTaskProducer.isFinished() || this.mTerminateSignal.get() ) {
            throw new IllegalStateException( "Mission is already terminated." );
        }
    }

    protected void beforeMultiTaskStart() {
        this.verifyIsTerminated();
    }

    protected void vitalizeSoloClew( int nCrewId ) {
        Crewnium crewnium = this.getParentHeist().newCrew( nCrewId );
        crewnium.getAffiliateThread().start();
        this.getTaskManager().add( crewnium  );
    }

    protected void vitalizeMultiTasks (){
        for ( int i = 0; i < this.mnMaximumThread; ++i ) {
            this.vitalizeSoloClew( i );
        }
    }

    protected void awaitTasksFinished() {
        try {
            if( this.mnAwaitFinishedMaxMillis > 0 ) {
                this.mTaskProducer.awaitProducerFinished( this.mnAwaitFinishedMaxMillis, TimeUnit.MILLISECONDS );
            }
            else {
                this.mTaskProducer.awaitProducerFinished();
            }
        }
        catch ( InterruptedException e ) {
            this.handleAliveException( e );
        }
        finally {
            this.getTaskManager().purge();
        }
    }

    @Override
    public void apoptosis() {
        if( this.mHierarchy == Hierarchy.Slave ) {
            synchronized ( this ) {
                if( !this.mTerminateSignal.get() ) {
                    this.mTerminateSignal.getAndSet( true );
                    this.mParentHeist.tracer().info( "[{} has been terminate.]", this.mParentHeist.heistName() );
                }
            }
        }
        else {
            this.getHeistletOrchestrator().terminate(); // Cascading terminate children.
        }
    }

    @Override
    public void terminate(){
        this.apoptosis(); //TODO
    }

    @Override
    public AtomicBoolean queryTerminationSignal() {
        return this.mTerminateSignal;
    }

    @Override
    public void joinStartMultiTasks() {
        this.beforeMultiTaskStart();
        this.vitalizeMultiTasks();
        this.awaitTasksFinished();
    }

    protected void handleAliveException( Exception e ) {
        this.getParentHeist().handleAliveException( e );
    }

    protected void handleKillException( Exception e ) throws IllegalStateException {
        this.getParentHeist().handleKillException( e );
    }

}
