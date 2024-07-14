package com.pinecone.hydra.orchestration;

public interface JumpController extends ProcessController {
    JumpController setJumpPoint( Object iter );

    Object getJumpPoint();
}
