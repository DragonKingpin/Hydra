package com.walnut.odin.processor.event;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskProcessorEvent extends Pinenut {

    TaskProcessorEventType getType();

    TaskProcessorEstablishment getEstablishment();

    long getClientId();

    String getNodeName();

    String getProcessorGuid();

    Map<String, String> getMetadata();

    String getReason();

    long getTimestamp();
}
