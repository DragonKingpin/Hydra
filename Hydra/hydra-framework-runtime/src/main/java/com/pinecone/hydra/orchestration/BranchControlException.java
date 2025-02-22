package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.prototype.Pinenut;

public class BranchControlException extends Exception implements Pinenut {
    public BranchControlException() {
        super();
    }

    public BranchControlException( String message ) {
        super( message );
    }
}
