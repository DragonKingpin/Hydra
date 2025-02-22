package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Chronum;
import com.pinecone.framework.system.executum.Executum;

public interface ParallelInstructation extends Instructation, Chronum {
    boolean isEnded();

    Exception lastException();

    void setLastException( Exception e );

    void terminate() ;

    void interrupt();

    void kill();

    boolean isDetached();

    boolean isJoined();

    ParallelInstructation setDetach();

    ParallelInstructation setJoin();

    long getMaxJoinMillis();

    ParallelInstructation setMaxJoinMillis( long join );

    Thread getMasterThread();

    default Executum tryGetMasterExecutum() {
        return null;
    }
}
