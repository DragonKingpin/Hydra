package com.orchestration;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.orchestration.parallel.ArchMasterParallelium;

public class SimpleParallelium extends ArchMasterParallelium {
    String mszToken;

    public SimpleParallelium( String szWho ) {
        this.mszToken = szWho;
    }

    @Override
    protected void doStart() {
        Debug.trace( "Hello hi, I am " + this.mszToken );
        Debug.trace( Thread.currentThread().getName() );

        Debug.sleep(100);
    }


    @Override
    protected void doTerminate() {

    }

    @Override
    protected void doRollback() {

    }
}
