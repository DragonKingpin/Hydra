package com.pinecone.framework.system;

import com.pinecone.framework.util.io.Tracer;

public interface Pinecore extends AsynSystem, CascadeSystem, ConformitySystem, RuntimeSystem {
    Tracer console();
}
