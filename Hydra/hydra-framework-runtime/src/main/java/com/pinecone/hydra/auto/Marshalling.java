package com.pinecone.hydra.auto;

import java.util.Collection;

public interface Marshalling extends Instructation {
    default void add( Instructation instructation ) {
        this.addLast( instructation );
    }

    void addLast( Instructation instructation );

    void addFirst( Instructation instructation );

    void erase( Instructation instructation );

    void prompt( Instructation instructation );

    Collection<Instructation> getInstructations();

    Collection<Instructation> getParallelInstructations();

    Collection<Instructation> getPriorInstructations();

    void terminate();
}
