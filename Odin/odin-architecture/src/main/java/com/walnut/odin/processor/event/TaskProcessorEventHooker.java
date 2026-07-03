package com.walnut.odin.processor.event;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskProcessorEventHooker extends Pinenut {

    void onTaskProcessorEvent( TaskProcessorEvent event );
}
