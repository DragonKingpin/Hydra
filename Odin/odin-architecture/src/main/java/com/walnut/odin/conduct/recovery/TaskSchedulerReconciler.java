package com.walnut.odin.conduct.recovery;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskSchedulerReconciler extends Pinenut {

    void reconcileLightweight( LocalDateTime targetTime );
}
