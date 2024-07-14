package com.pinecone.hydra.orchestration;

import com.pinecone.hydra.orchestration.parallel.ParallelExertion;
import com.pinecone.hydra.orchestration.parallel.WrappedMasterParallelium;

public class ParallelAction extends ArchParallel {
    public ParallelAction() {
        super();
    }

    public static ParallelExertion wrap( Exertion exertion ) {
        return new WrappedMasterParallelium( exertion );
    }
}
