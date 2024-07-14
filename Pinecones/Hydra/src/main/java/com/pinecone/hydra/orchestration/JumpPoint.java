package com.pinecone.hydra.orchestration;

public class JumpPoint extends ArchIrrevocableController implements JumpController {
    protected Object mJumpPoint;

    public JumpPoint( Object jumpPoint ) {
        super();
        this.mJumpPoint = jumpPoint;
    }

    @Override
    public JumpPoint setJumpPoint(Object mJumpPoint) {
        this.mJumpPoint = mJumpPoint;
        return this;
    }

    @Override
    public Object getJumpPoint() {
        return this.mJumpPoint;
    }

    @Override
    public void call() throws BranchControlException {
        this.start();
    }
}
