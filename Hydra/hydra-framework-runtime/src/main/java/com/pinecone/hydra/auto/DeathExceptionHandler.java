package com.pinecone.hydra.auto;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;

public class DeathExceptionHandler implements ExceptionHandler {
    protected Automatron mAutomatron;

    public DeathExceptionHandler( Automatron automatron ) {
        this.mAutomatron = automatron;
    }

    @Override
    public void handle( Exception e ) throws ProxyProvokeHandleException, InstantKillException, AbortException, ContinueException {
        if( e instanceof InstantKillException ) {
            Debug.info( "[NOTICE] <Caused by InstantKillException>" );
            //e.printStackTrace();
            throw new InstantKillException( e ) ;
        }
        else if( e instanceof ContinueException ) {
            throw (ContinueException) e;
        }
        else if( e instanceof AbortException ) {
            throw (AbortException) e;
        }
        else {
            e.printStackTrace();
            throw new ProxyProvokeHandleException( e );
        }
    }
}
