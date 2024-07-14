package com.orchestration;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.orchestration.Exertium;

public class SimpleExertium extends Exertium {
    String mszToken;

    public SimpleExertium( String szWho ) {
        this.mszToken = szWho;
    }

    @Override
    protected void doStart() {
        Debug.trace( "Hello hi, I am " + this.mszToken );
        Debug.sleep(50);
    }
}
