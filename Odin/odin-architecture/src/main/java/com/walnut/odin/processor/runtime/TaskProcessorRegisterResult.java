package com.walnut.odin.processor.runtime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;
import com.walnut.odin.processor.event.TaskProcessorEstablishment;

public interface TaskProcessorRegisterResult extends Pinenut {

    TaskProcessorEstablishment getEstablishment();

    boolean isAccepted();

    String getReason();

    TaskProcessorEntity getIncorporatedEntity();

    AnonymousTaskProcessorRegistration getAnonymousRegistration();
}
