package com.pinecone.slime.cache.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public interface IterableDictCachePage<V > extends CountDictCachePage<V >, Iterable {
    default Iterator<? > iterator() {
        return this.entrySet().iterator();
    }

    Set<? > entrySet();

    Collection<V > values();
}
