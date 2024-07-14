package com.pinecone.slime.source.indexable;

import java.util.Iterator;
import java.util.Map;

public interface IndexableIterableManipulator<K, V > extends IndexableDataManipulator<K, V > {
    Iterator<K > keysIterator( IndexableTargetScopeMeta meta );

    Iterator<Map.Entry<K, V > >  iterator( IndexableTargetScopeMeta meta );
}
