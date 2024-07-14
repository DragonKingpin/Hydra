package com.pinecone.framework.unit;

import com.pinecone.framework.unit.multi.MultiCollectionProxyMap;

import java.util.List;

public interface MultiValueMap<K, V > extends MultiCollectionProxyMap<K, V, List<V > > {
}
