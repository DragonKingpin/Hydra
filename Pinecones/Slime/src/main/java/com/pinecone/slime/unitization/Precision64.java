package com.pinecone.slime.unitization;

public class Precision64 extends Number implements NumPrecision {
    protected long precision;

    public Precision64( long precision ) {
        this.precision = precision;
    }

    @Override
    public Number numericValue() {
        return this.precision;
    }

    @Override
    public long longValue() {
        return this.precision;
    }

    @Override
    public int intValue() {
        return (int) this.precision;
    }

    @Override
    public float floatValue() {
        return (float) this.precision;
    }

    @Override
    public double doubleValue() {
        return (double) this.precision;
    }

    @Override
    public String toString() {
        return Long.toString( this.precision );
    }

    @Override
    public String toJSONString() {
        return this.toString();
    }
}
