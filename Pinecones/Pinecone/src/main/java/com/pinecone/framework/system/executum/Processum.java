package com.pinecone.framework.system.executum;

import java.time.LocalDateTime;
import java.util.Map;

public interface Processum extends Executum {
    Map<Long, Executum >   getOwnThreadGroup();

    default Thread         rootThread() {
        return this.getAffiliateThread();
    }

    default boolean        isOnMainThread() {
        return this.rootThread() == null || this.rootThread() == this.parentSystem().getProcessMainThread();
    }

    TaskManager            getTaskManager();


    LocalDateTime          getCreateTime() ;

    LocalDateTime          getStartTime() ;


}
