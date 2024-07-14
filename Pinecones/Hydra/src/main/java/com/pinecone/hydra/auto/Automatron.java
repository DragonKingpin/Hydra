package com.pinecone.hydra.auto;

import com.pinecone.framework.system.executum.Processum;

public interface Automatron extends Processum {
    void start();

    void join() throws InterruptedException;

    void join( long millis ) throws InterruptedException;

    // Add to pipeline tail
    void command ( Instructation instructation );

    // Add to pipeline front
    void prompt  ( Instructation instructation );

    void withdraw  ( Instructation instructation );

    default void terminate() {
        this.prompt( KernelInstructation.DIE );
    }

    boolean isEnded();

    Exception getLastException();

    ExceptionHandler getExceptionHandler();

    Automatron setExceptionHandler( ExceptionHandler handler );
}
