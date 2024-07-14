package com.pinecone.slime.unitization;

public class MinMaxRange64 implements MinMaxRange {
    protected long           mnMin;
    protected long           mnMax;
    protected Precision      mPrimePrecision;  // Precision(1)=> for the array-like structure.

    public MinMaxRange64( long nMin, long nMax, Precision primePrecision ) {
        this.mnMin           = nMin;
        this.mnMax           = nMax;
        this.mPrimePrecision = primePrecision;
    }

    public MinMaxRange64( long nMin, long nMax ) {
        this( nMin, nMax, NumPrecision.PRECISION_64_1 );
    }


    @Override
    public Long getMin() {
        return this.mnMin;
    }

    @Override
    public Long getMax() {
        return this.mnMax;
    }

    @Override
    public void setRange( Number min, Number max ){
        this.setMin( min );
        this.setMax( max );
    }

    @Override
    public void setMin  ( Number min ){
        this.mnMin = min.longValue();
    }

    @Override
    public void setMax  ( Number max ) {
        this.mnMax = max.longValue();
    }

    @Override
    public Long span() {
        return this.mnMax - this.mnMin;
    }

    @Override
    public Precision getPrimePrecision() {
        return this.mPrimePrecision;
    }

    @Override
    public boolean contains( Range that ) {
        MinMaxRange range = (MinMaxRange) that;
        return this.mnMin <= range.getMin().longValue() && this.mnMax >= range.getMax().longValue();
    }

    @Override
    public boolean contains( Object elm ) {
        long e = ( (Number) elm ).longValue();
        return this.mnMin <= e && this.mnMax >= e;
    }

    @Override
    public int compareTo( MinMaxRange o ) {
        if ( this == o ) {
            return 0;
        }
        if ( o == null ) {
            return 1;
        }

        int minCompare = Long.compare( this.mnMin, o.getMin().longValue() );
        if ( minCompare != 0 ) {
            return minCompare;
        }

        return Long.compare( this.mnMax, o.getMax().longValue() );
    }

    @Override
    public boolean equals( Object obj ) {
        if( super.equals( obj ) ) {
            return true;
        }

        if( obj instanceof MinMaxRange ) {
            return this.getMin().equals( ((MinMaxRange) obj).getMin().longValue() ) && this.getMax().equals( ((MinMaxRange) obj).getMax().longValue() );
        }
        return false;
    }
}
