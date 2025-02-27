package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.executum.Chronum;
import com.pinecone.framework.system.prototype.Pinenut;

public interface Exertion extends Pinenut, GraphNode, Chronum {
    String getName();

    void setName( String name );

    IntegrityLevel getIntegrityLevel();

    void setIntegrityLevel( IntegrityLevel level );

    void reset();

    void start();

    void terminate();

    void rollback();

    void setDefaultRollback( boolean b );

    boolean isDefaultRollback();

    ExertionStatus getStatus();

    default boolean isFinished(){
        return this.getStatus() == ExertionStatus.FINISHED;
    }

    default boolean isIntrrupted(){
        return this.getStatus() == ExertionStatus.INTERRUPTED;
    }

    default boolean isTerminated(){
        return this.getStatus() == ExertionStatus.TERMINATED;
    }

    // The running is end.
    default boolean isEnded() {
        return this.getStatus().isEnded();
    }

    Exception getLastError();
}
