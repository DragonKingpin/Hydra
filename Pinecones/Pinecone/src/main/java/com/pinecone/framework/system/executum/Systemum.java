package com.pinecone.framework.system.executum;

import java.util.Map;
import java.util.Set;

public interface Systemum extends Processum {
    String       getName();

    int          getId();

    Thread       getAffiliateThread();

    Thread       getProcessMainThread() ;

    default boolean  isMainThreadSystem() {
        return this.getAffiliateThread() == this.getProcessMainThread();
    }

    default Set<Thread > fetchAllProcessThreads() {
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        return allThreads.keySet();
    }
}
