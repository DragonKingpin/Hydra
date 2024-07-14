package com.pinecone.slime.chunk;

import com.pinecone.slime.unitization.Range;
import com.pinecone.slime.unitization.Precision;

/**
 * Continum Chunk
 */
public interface Continunk extends Chunk {
    Range getRange();

    Precision size();
}
