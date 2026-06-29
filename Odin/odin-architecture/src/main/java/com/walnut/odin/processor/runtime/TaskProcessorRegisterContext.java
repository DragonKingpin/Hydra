package com.walnut.odin.processor.runtime;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskProcessorRegisterContext extends Pinenut {

    String getNodeName();

    long getClientId();

    Map<String, String> getMetadata();
}
