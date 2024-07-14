package com.pinecone.slime.unitization;

public class PartialOrderRange<T extends Comparable<T > > implements PartialRange<T > {
    protected T mMin;
    protected T mMax;

    public PartialOrderRange( T min, T max ) {
        this.mMin           = min;
        this.mMax           = max;
    }

    @Override
    public T getMin(){
        return this.mMin;
    }

    @Override
    public T getMax(){
        return this.mMax;
    }

    @Override
    public void   setRange( T min, T max ){
        this.mMin = min;
        this.mMax = max;
    }

    @Override
    public void   setMin  ( T min ){
        this.mMin = min;
    }

    @Override
    public void   setMax  ( T max ){
        this.mMax = max;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public boolean contains( Range that ) {
        if ( !( that instanceof PartialOrderRange ) ) {
            throw new ClassCastException("Range is not a PartialOrderRange.");
        }
        PartialOrderRange<T> range = (PartialOrderRange<T>) that;

        return ( range.getMin().compareTo( this.mMin ) >= 0) && ( range.getMax().compareTo( this.mMax ) <= 0 );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public boolean equals( Object obj ) {
        if( super.equals( obj ) ) {
            return true;
        }

        if( obj instanceof PartialOrderRange ) {
            PartialOrderRange<T> range = (PartialOrderRange<T>) obj;
            return ( range.getMin().compareTo( this.mMin ) == 0) && ( range.getMax().compareTo( this.mMax ) == 0 );
        }
        return false;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
