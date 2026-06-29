package com.walnut.odin.processor.runtime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;

public interface TaskProcessorRuntime extends Pinenut {

    TaskProcessorRegisterResult register( TaskProcessorRegisterContext context );

    TaskProcessorUnregisterResult unregister( long nClientId );

    AnonymousTaskProcessorRegistry anonymousProcessorRegistry();

    TaskProcessorEventHookRegistry eventHookRegistry();
}
