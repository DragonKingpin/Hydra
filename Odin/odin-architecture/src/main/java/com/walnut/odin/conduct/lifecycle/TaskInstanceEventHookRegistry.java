package com.walnut.odin.conduct.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskInstanceEventHookRegistry extends Pinenut {

    void register( TaskInstanceEventHook hook );

    void deregister( TaskInstanceEventHook hook );

    void dispatch( TaskInstanceTransitionResult result );
}
