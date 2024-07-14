package com.pinecone.hydra.auto;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.concurrent.TimeoutException;

public interface MationInvoker extends Pinenut {
    default boolean isInstructationViolation( Suggestation suggestation ) {
        return false;
    }

    default void invoke   ( Instructation instructation ) throws Exception {
        try {
            if( instructation instanceof Suggestation ) {
                if( this.isInstructationViolation( (Suggestation)instructation ) ){
                    ((Suggestation) instructation).setIgnoredReason( IgnoredReason.Violation );
                    return;
                }
            }
            instructation.execute();
        }
        catch ( ContinueException c ) { // Just continue.
            if( instructation instanceof Suggestation ) {
                Suggestation suggestation = ((Suggestation) instructation);
                suggestation.setIgnoredReason( IgnoredReason.Abort );
                suggestation.setLastException( c );
            }
            else {
                throw c;
            }
        }
        catch ( Exception e ) {
            if( instructation instanceof Suggestation ) {
                Suggestation suggestation = ((Suggestation) instructation);
                suggestation.setIgnoredReason( IgnoredReason.Exception );
                suggestation.setLastException( e );
            }
            else if( instructation instanceof ParallelInstructation ) {
                ((ParallelInstructation) instructation).setLastException( e );
            }
            else {
                throw e;
            }
        }
    }

    default void terminate( ParallelInstructation instructation ) throws InterruptedException {
        instructation.interrupt();

        if( this.getMaxInterruptMillis() > 0 ) {
            long startApoptosisMillis = System.currentTimeMillis();
            long maxApoptosisMillis   = this.getMaxInterruptMillis();
            while ( System.currentTimeMillis() - startApoptosisMillis < maxApoptosisMillis ) {
                if (instructation.isEnded()) {
                    return;
                }

                Thread.sleep( 50 );
            }
        }

        // If the instructation is still not ended, kill it
        if ( !instructation.isEnded() ) {
            instructation.kill();
        }
    }

    long getMaxExecutionMillis();

    long getMaxInterruptMillis();

    void setMaxExecutionMillis( long maxExecutionMillis );

    void setMaxInterruptMillis( long maxInterruptMillis );

    default void checkTimeout( ParallelInstructation instructation ) throws TimeoutException {
        if ( instructation.getExecutedMillis() > this.getMaxExecutionMillis() ) {
            throw new TimeoutException( "Execution exceeded max time limit of " + this.getMaxExecutionMillis() + " milliseconds." );
        }
    }
}
