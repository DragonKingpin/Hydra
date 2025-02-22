package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Executum;

public class AutomatronMationInvoker extends GenericMationInvoker {
    protected Automatron mAutomatron;

    public AutomatronMationInvoker( long maxExecutionMillis, long maxInterruptMillis, Automatron automatron ) {
        super( maxExecutionMillis, maxInterruptMillis );

        this.mAutomatron = automatron;
    }

    public AutomatronMationInvoker( Automatron automatron ) {
        this( Long.MAX_VALUE, -1, automatron );
    }

    @Override
    public void invoke   ( Instructation instructation ) throws Exception {
        Executum executum = null;
        if( instructation instanceof ParallelInstructation ) {
            executum = ((ParallelInstructation) instructation).tryGetMasterExecutum();
            if( executum != null ) {
                this.mAutomatron.getTaskManager().add( executum );
            }
        }

        try{
            super.invoke( instructation );
        }
        catch ( Exception e ) {
            if( executum != null ) {
                this.mAutomatron.getTaskManager().erase( executum );
            }

            throw e;
        }
    }

}
