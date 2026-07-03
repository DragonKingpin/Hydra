package com.walnut.odin.processor.anonymous;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public interface AnonymousTaskProcessorRegistration extends Pinenut {

    long getClientId();

    String getNodeName();

    String getAlias();

    String getBizPath();

    String getRuntime();

    Collection<String> getExecCaps();

    Map<String, String> getMetadata();

    long getRegisterTime();

    long getLastUpdateTime();
}
