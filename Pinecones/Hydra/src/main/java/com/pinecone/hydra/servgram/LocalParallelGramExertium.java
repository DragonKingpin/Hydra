package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.hydra.orchestration.parallel.ArchMasterParallelium;

public class LocalParallelGramExertium extends ArchMasterParallelium {
    protected Servgram              mWrapServgram;
    protected ServgramOrchestrator  mOrchestrator;

    public LocalParallelGramExertium( ServgramOrchestrator orchestrator, Servgram servgram ) {
        this.mWrapServgram = servgram;
        this.mOrchestrator = orchestrator;
        this.setName( servgram.getName() );
    }

    @Override
    protected void doStart() {
        try{
            Thread thisThread = this.getMasterExecutum().getAffiliateThread();
            thisThread.setName( this.nomenclature( thisThread ) );

            this.mWrapServgram.execute();
        }
        catch ( Exception e ) {
            throw new ProvokeHandleException( e );
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
