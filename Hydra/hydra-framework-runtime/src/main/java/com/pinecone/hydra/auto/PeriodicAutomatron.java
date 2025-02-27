package com.pinecone.hydra.auto;

import java.util.Collection;

public interface PeriodicAutomatron extends Automatron {
    long getPeriodMillis() ;

    void setPeriodMillis( long periodMillis ) ;

    Thread getMasterThread();

    int bufferSize();

    Collection<Instructation > getBuffer();

    Marshalling getMarshalling();
}
