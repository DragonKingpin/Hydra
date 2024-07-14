package com.pinecone.slime.chunk.scheduler;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Map;

public interface ChunkRegister<K, V > extends Pinenut, Map<K, V> {
}
