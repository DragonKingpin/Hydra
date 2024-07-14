package com.pinecone.framework.unit;

import java.util.ListIterator;
import java.util.SortedMap;

public interface ListedSortedMap<K,V> extends SortedMap<K,V> {
    ListIterator<Entry<K, V> > listIterator(int index );
}
