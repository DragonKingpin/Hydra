package com.walnut.odin.conduct.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskInstanceEventHook extends Pinenut {

    void onTaskInstanceTransition( TaskInstanceTransitionResult result );
}
