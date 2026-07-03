package com.walnut.odin.conduct.lifecycle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KernelTaskInstanceEventHookRegistry implements TaskInstanceEventHookRegistry {

    protected List<TaskInstanceEventHook> mHooks;

    public KernelTaskInstanceEventHookRegistry() {
        this.mHooks = new CopyOnWriteArrayList<>();
    }

    @Override
    public void register( TaskInstanceEventHook hook ) {
        if ( hook == null ) {
            return;
        }
        if ( this.mHooks.contains( hook ) ) {
            return;
        }
        this.mHooks.add( hook );
    }

    @Override
    public void deregister( TaskInstanceEventHook hook ) {
        if ( hook == null ) {
            return;
        }
        this.mHooks.remove( hook );
    }

    @Override
    public void dispatch( TaskInstanceTransitionResult result ) {
        if ( result == null || !result.isSucceeded() ) {
            return;
        }
        for ( TaskInstanceEventHook hook : this.mHooks ) {
            hook.onTaskInstanceTransition( result );
        }
    }
}
