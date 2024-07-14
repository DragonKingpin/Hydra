package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Executum;

public class GenericMarshalling extends ArchSequentialMarshalling {
    protected Automatron   mAutomatron;

    public GenericMarshalling( Automatron automatron, MationInvoker invoker ) {
        super( invoker );

        this.mAutomatron = automatron;
    }

    public GenericMarshalling( Automatron automatron ) {
        this( automatron, new AutomatronMationInvoker( automatron ) );
    }

    public GenericMarshalling() {
        this( null, new GenericMationInvoker() );
    }
}
