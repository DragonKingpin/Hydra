package com.pinecone.hydra.auto;

import com.pinecone.framework.util.Debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;
import java.util.concurrent.TimeoutException;

public abstract class ArchSequentialMarshalling implements Marshalling {
    protected List<Instructation >   mInstructations;
    protected Deque<Instructation >  mPriorInstructations;
    protected List<Instructation >   mParallelInstructations;
    protected MationInvoker          mMationInvoker;

    protected ArchSequentialMarshalling( List<Instructation> instructations, List<Instructation> parallelInstructations, Deque<Instructation> priorInstructations, MationInvoker invoker ) {
        this.mParallelInstructations = parallelInstructations;
        this.mPriorInstructations    = priorInstructations;
        this.mInstructations         = instructations;
        this.mMationInvoker          = invoker;
    }

    protected ArchSequentialMarshalling( MationInvoker invoker ) {
        this( new ArrayList<>(), new ArrayList<>(), new LinkedList<>(), invoker );
    }

    protected ArchSequentialMarshalling() {
        this( new GenericMationInvoker() );
    }

    @Override
    public Collection<Instructation> getInstructations() {
        return this.mInstructations;
    }

    @Override
    public Collection<Instructation> getParallelInstructations() {
        return this.mParallelInstructations;
    }

    @Override
    public Collection<Instructation> getPriorInstructations() {
        return this.mPriorInstructations;
    }

    @Override
    public void addLast( Instructation instructation ) {
        if( instructation instanceof InstantInstructation ) {
            this.prompt( instructation );
        }
        else {
            this.mInstructations.add( instructation );
            if ( instructation instanceof ParallelInstructation ) {
                this.mParallelInstructations.add( instructation );
            }
        }
    }

    @Override
    public void addFirst( Instructation instructation ) {
        if( instructation instanceof InstantInstructation ) {
            this.prompt( instructation );
        }
        else {
            this.mInstructations.add( 0, instructation );
            if ( instructation instanceof ParallelInstructation ) {
                this.mParallelInstructations.add( 0, instructation );
            }
        }
    }

    @Override
    public void erase( Instructation instructation ) {
        if( instructation instanceof InstantInstructation ) {
            this.mPriorInstructations.remove( instructation );
        }
        else {
            this.mInstructations.remove( instructation );
            if ( instructation instanceof ParallelInstructation ) {
                this.mParallelInstructations.remove( instructation );
            }
        }
    }

    @Override
    public void prompt( Instructation instructation ) {
        this.mPriorInstructations.addFirst( instructation );
    }

    protected boolean executeKernelInstructations( Instructation instruction ) throws Exception {
        if( instruction == KernelInstructation.DIE ) {
            instruction.execute();
            return true;
        }
        return false;
    }

    protected void executePriorInstructations( boolean bOnlyTryCallKernel ) throws Exception {
        for( Instructation instruction : this.mPriorInstructations ) {
            if( !this.executeKernelInstructations( instruction ) && !bOnlyTryCallKernel ){
                this.mMationInvoker.invoke( instruction );
            }
        }
    }

    @Override
    public void execute() throws Exception {
        List<Instructation> children = this.mInstructations;

        try{
            this.executePriorInstructations( false );
            for( Instructation instruction : children ) {
                this.executePriorInstructations( true );
                if( Thread.currentThread().isInterrupted() ) {
                    throw new InterruptedException( "Interrupt termination." );
                }
                this.mMationInvoker.invoke( instruction );
            }
        }
        catch ( InstantKillException e ) {
            this.terminate();
            throw e;
        }

        this.waitForParallelInstructations();
    }

    @Override
    public void terminate() {
        for ( Instructation instruction : this.mParallelInstructations ) {
            ParallelInstructation parallelInstruction = (ParallelInstructation) instruction;
            parallelInstruction.terminate();
        }
    }

    protected void waitForParallelInstructations() throws InterruptedException, TimeoutException {
        boolean allEnded;
        do {
            allEnded = true;
            for ( Instructation instruction : this.mParallelInstructations ) {
                if( Thread.currentThread().isInterrupted() ) {
                    this.terminate();
                    throw new InterruptedException( "Interrupt termination." );
                }

                ParallelInstructation parallelInstruction = (ParallelInstructation) instruction;
                if ( !parallelInstruction.isEnded() ) {
                    allEnded = false;
                    try{
                        this.mMationInvoker.checkTimeout( parallelInstruction );
                    }
                    catch ( TimeoutException e ) {
                        this.mMationInvoker.terminate( parallelInstruction );
                        parallelInstruction.setLastException( e );

                        if( parallelInstruction instanceof ParallelSuggestation ) {
                            ((ParallelSuggestation) parallelInstruction).setIgnoredReason( IgnoredReason.Overtime );
                        }
                        else {
                            throw e;
                        }
                    }

                    Thread.sleep( 50 );
                }
            }
        }
        while ( !allEnded );
    }
}
