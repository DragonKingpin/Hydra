package com.pinecone.hydra.orchestration;

import com.pinecone.hydra.orchestration.parallel.ParallelExertion;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Phaser;

public abstract class ArchParallel extends ArchSequential implements Parallel {
    protected Phaser activePhaser           = new Phaser(1);

    ArchParallel() {
        super();
    }

    @Override
    public void reset() {
        this.getExertium().reset();
    }

    @Override
    protected void waiting_exertions_pool_synchronized() {
        this.activePhaser.arriveAndAwaitAdvance();

        List<GraphNode > children = this.getChildren();
        Iterator<GraphNode > iter = children.iterator();

        while ( iter.hasNext() ) {
            Exertion exertion = (Exertion) iter.next();

            if( !exertion.isFinished() ){
                throw new UnfulfilledActionException( "Illegal transaction status, unfulfilled exertion founded." , exertion );
            }
        }
    }

    @Override
    protected void waiting_for_single_exertion( Exertion exertion ) {
        if( exertion instanceof ParallelExertion ) {
            ParallelExertion pe = ( ParallelExertion ) exertion;
            if( pe.isForceSynchronized() ) {
                synchronized ( pe.getFinaleLock() ) {
                    if( pe.getMasterExecutum().getAffiliateThread().isAlive() ) {
                        try {
                            long nMax = pe.getMaximumExecutionTime();
                            if( nMax > 0 ) {
                                pe.getFinaleLock().wait( nMax );
                            }
                            else {
                                pe.getFinaleLock().wait( );
                            }
                        }
                        catch ( InterruptedException e ) {
                            throw new UnfulfilledActionException( e );
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean is_dfa_status_finished_check_required( Exertion exertion ) {
        if( exertion instanceof ParallelExertion ) {
            return ((ParallelExertion) exertion).isForceSynchronized();
        }
        return true;
    }

    @Override
    public void notifyFinished( Exertion exertion ){
        this.activePhaser.arriveAndDeregister();
    }

    @Override
    public void notifyExecuting ( Exertion exertion ) {
        this.activePhaser.register();
    }
}
