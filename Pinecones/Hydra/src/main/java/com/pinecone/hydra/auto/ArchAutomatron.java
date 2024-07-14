package com.pinecone.hydra.auto;

import com.pinecone.framework.system.GenericMasterTaskManager;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Processum;

public abstract class ArchAutomatron extends ArchProcessum implements Automatron {
    private   ExceptionHandler            mExceptionHandler;
    protected Exception                   mLastException;

    protected ArchAutomatron( String szName, Processum parent, ExceptionHandler handler ) {
        super( szName, parent );

        if( handler == null ) {
            handler = new DeathExceptionHandler( this );
        }

        this.mTaskManager      = new GenericMasterTaskManager( this );
        this.mExceptionHandler = handler;
    }

    protected ArchAutomatron( String szName, Processum parent ) {
        this( szName, parent, null );
    }

    protected void handleException( Exception e ) throws ProxyProvokeHandleException, InstantKillException, AbortException, ContinueException {
        this.mLastException = e;

        try{
            this.getExceptionHandler().handle( e );
        }
        catch ( ContinueException c ) {
            throw c;
        }
        catch ( RuntimeException e1 ) {
            this.intoEnded();
            throw e1;
        }
    }

    protected abstract void intoEnded() ;

    @Override
    public Exception getLastException() {
        return this.mLastException;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return this.mExceptionHandler;
    }

    @Override
    public Automatron setExceptionHandler( ExceptionHandler handler ) {
        this.mExceptionHandler = handler;
        return this;
    }
}
