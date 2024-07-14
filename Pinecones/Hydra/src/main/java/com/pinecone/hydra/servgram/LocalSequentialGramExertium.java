package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.ApoptosisRejectSignalException;
import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.orchestration.ArchExertion;
import com.pinecone.hydra.orchestration.ExertionStatus;

public class LocalSequentialGramExertium extends ArchExertion {
    protected Servgram              mWrapServgram;
    protected ServgramOrchestrator  mOrchestrator;

    public LocalSequentialGramExertium( ServgramOrchestrator orchestrator, Servgram servgram ) {
        this.mWrapServgram = servgram;
        this.mOrchestrator = orchestrator;
        this.setName( servgram.getName() );
    }

    @Override
    protected void doStart() {
        try{
            this.mWrapServgram.execute();
        }
        catch ( Exception e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    protected void doTerminate() {
        LocalSequentialGramExertium.terminate( this.mWrapServgram );
    }

    @Override
    protected void doRollback() {
        throw new NotImplementedException();
    }

    protected static void terminate( Servgram servgram ) throws ProxyProvokeHandleException {
        try{
            try{
                servgram.terminate();
            }
            catch ( ApoptosisRejectSignalException e ) {
                if( servgram instanceof Servgramium ) {
                    ((Servgramium) servgram).kill();
                }
            }
        }
        catch ( Exception e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    protected void intoStart() {
        super.intoStart();
        this.notifyExecuting();
    }

    @Override
    protected void intoFinished() {
        super.intoFinished();
        this.notifyFinished();
    }

    @Override
    protected void intoTerminated() {
        super.intoTerminated();
        this.notifyFinished();
    }

    @Override
    protected void intoRollback() {
        super.intoRollback();
        this.notifyExecuting();
    }

    @Override
    protected void intoError( Exception e ) {
        super.intoError( e );
        this.notifyFinished();
    }

    protected void notifyExecuting() {
        if( this.mWrapServgram instanceof Executum ) {
            this.mOrchestrator.notifyExecuting( (Executum)this.mWrapServgram );
        }
    }

    protected void notifyFinished() {
        if( this.mWrapServgram instanceof Executum ) {
            this.mOrchestrator.notifyFinished( (Executum)this.mWrapServgram );
        }
    }

}
