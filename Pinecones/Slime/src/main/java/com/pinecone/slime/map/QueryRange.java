package com.pinecone.slime.map;

import com.pinecone.slime.unitization.PartialRange;

public interface QueryRange<T extends Comparable<T > > extends PartialRange<T > {
    String getRangeKey();
}
