package com.pinecone.hydra.system;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.MultiScopeMap;

public interface ScopedSystem extends Pinenut {
    MultiScopeMap<String, Object > getGlobalConfigScope();
}
