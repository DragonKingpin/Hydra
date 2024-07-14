package com.pinecone.hydra.orchestration.parallel;

import com.pinecone.framework.system.executum.Executum;
import com.pinecone.hydra.orchestration.Exertion;

public interface ParallelExertion extends Exertion {
    Object getFinaleLock();

    boolean isForceSynchronized();

    ParallelExertion setForceSynchronized();

    ParallelExertion setNoneSynchronized();

    /**
     * FinaleLock is used to control the final-synchronized of the parent sequential action-list
     * Explicitly release this lock during the runtime, which can directly removes the buff of the 'ForceSynchronized'.
     */
    void releaseFinaleLock();

    boolean isDetached();

    boolean isJoined();

    ParallelExertion setDetach();

    ParallelExertion setJoin();

    Executum getMasterExecutum();

    ParallelExertion setMaximumExecutionTime( long millis );

    long getMaximumExecutionTime();

    String   nomenclature   ( Thread   that );
}
