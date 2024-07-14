package com.pinecone.framework.unit.top;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Collection;

public interface MultiToptronValueAdapter<E > extends Pinenut {
    Collection<E > newCollection();
}
