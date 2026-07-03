package com.walnut.odin.processor.runtime;

import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;
import com.walnut.odin.processor.runtime.TaskProcessorUnregisterResult;

public class GenericTaskProcessorUnregisterResult implements TaskProcessorUnregisterResult {

    protected TaskExecutionProcessor              mIncorporatedProcessor;
    protected AnonymousTaskProcessorRegistration  mAnonymousRegistration;

    public GenericTaskProcessorUnregisterResult(
            TaskExecutionProcessor incorporatedProcessor,
            AnonymousTaskProcessorRegistration anonymousRegistration
    ) {
        this.mIncorporatedProcessor = incorporatedProcessor;
        this.mAnonymousRegistration = anonymousRegistration;
    }

    public static GenericTaskProcessorUnregisterResult of(
            TaskExecutionProcessor incorporatedProcessor,
            AnonymousTaskProcessorRegistration anonymousRegistration
    ) {
        return new GenericTaskProcessorUnregisterResult( incorporatedProcessor, anonymousRegistration );
    }

    @Override
    public TaskExecutionProcessor getIncorporatedProcessor() {
        return this.mIncorporatedProcessor;
    }

    @Override
    public AnonymousTaskProcessorRegistration getAnonymousRegistration() {
        return this.mAnonymousRegistration;
    }

    @Override
    public boolean hasAny() {
        return this.mIncorporatedProcessor != null || this.mAnonymousRegistration != null;
    }
}
