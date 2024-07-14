package com.pinecone.slime.unitization;

public interface NumPrecision extends Precision {
    NumPrecision PRECISION_64_1 = new Precision64(1);

    Number numericValue();

    long longValue();
}
