package com.pinecone.framework.system.executum;

import java.util.Map;

public interface Processum extends Executum {
    Map<Integer, Executum > getOwnThreadGroup();

    default Thread         rootThread() {
        return this.getAffiliateThread();
    }

    default boolean        isOnMainThread() {
        return this.rootThread() == null || this.rootThread() == this.getSystem().getProcessMainThread();
    }

    TaskManager            getTaskManager();
}
