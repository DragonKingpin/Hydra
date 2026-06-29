package com.walnut.odin.processor.event;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskProcessorEventHookRegistry extends Pinenut {

    void addHooker( TaskProcessorEventHooker hooker );

    void removeHooker( TaskProcessorEventHooker hooker );

    Collection<TaskProcessorEventHooker> hookers();

    void dispatch( TaskProcessorEvent event );
}
