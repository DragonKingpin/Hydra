package com.pinecone.slime.chunk;

import com.pinecone.slime.unitization.NumPrecision;

public interface RangedPage extends ContiguousPage {
    @Override
    NumPrecision size();

    @Override
    default long elementSize() {
        return this.size().longValue();
    }
}
