package com.walnut.odin.processor.runtime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;

public interface TaskProcessorUnregisterResult extends Pinenut {

    TaskExecutionProcessor getIncorporatedProcessor();

    AnonymousTaskProcessorRegistration getAnonymousRegistration();

    boolean hasAny();
}
