package com.pinecone.framework.system.executum;

public interface EventedTaskManager extends TaskManager {
    void notifyFinished  ( Executum that );

    void notifyExecuting ( Executum that );
}
