package com.pinecone.hydra.orchestration;

public class InstantJumpOutBranchException extends BranchControlException {
    public InstantJumpOutBranchException() {
        super();
    }

    public InstantJumpOutBranchException( String message ) {
        super( message );
    }
}
