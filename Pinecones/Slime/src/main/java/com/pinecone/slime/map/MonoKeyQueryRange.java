package com.pinecone.slime.map;

import com.pinecone.slime.unitization.PartialOrderRange;

public class MonoKeyQueryRange<T extends Comparable<T > > extends PartialOrderRange<T > implements QueryRange<T > {
    protected String mszRangeKey;

    public MonoKeyQueryRange( T min, T max, String szRangeKey ) {
        super( min, max );
        this.mszRangeKey = szRangeKey;
    }

    @Override
    public String getRangeKey() {
        return this.mszRangeKey;
    }
}
