package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.functions.Executable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Pinecone Ursus For Java Executum
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Executum vs Executor
 *  1.  Executor is just a function, that ignores the specific thread it executing on.
 *  2.  Executum is a thread based executable object, that has its own specific execute threads.
 *  2.1 Executum can own its thread group, which just like a process [Processum].
 *  2.2 Executum is a sophisticated task, which is focus on specific task-group or scheme.
 *  *****************************************************************************************
 */
public interface Executum extends Executable, Lifecycle {
    String              getName();

    void                setName( String szName );

    int                 getId();

    RuntimeSystem       getSystem();

    Executum            parentExecutum();

    Executum            setThreadAffinity( Thread affinity );

    Thread              getAffiliateThread();

    default boolean     isSystemExecutum() {
        return this instanceof Systemum;
    }

    default boolean     isMainThreadExecutum() {
        return this.getAffiliateThread() == this.getSystem().getProcessMainThread();
    }

    boolean             isTerminated();

    AtomicInteger AutoIncrementId     = new AtomicInteger( 0 );

    static int nextAutoIncrementId() {
        return Executum.AutoIncrementId.getAndIncrement();
    }
}
