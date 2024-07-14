package com.pinecone.slime.unitization;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface MinMaxRange extends LinerRange, Comparable<MinMaxRange > {
    Number getMin();

    Number getMax();

    void   setRange( Number min, Number max );

    void   setMin  ( Number min );

    void   setMax  ( Number max );

    @Override
    default String toJSONString() {
        return String.format(
                "{\"class\":\"%s\",\"min\":%s,\"max\":%s}", this.className(), this.getMin(), this.getMax()
        );
    }

    @Override
    default int compareTo( MinMaxRange o ) {
        if ( this == o ) {
            return 0;
        }
        if ( o == null ) {
            return 1;
        }

        Number min = this.getMin();
        if( min instanceof Double || min instanceof Float ) {
            int minCompare = Double.compare( this.getMin().doubleValue(), o.getMin().doubleValue() );
            if ( minCompare != 0 ) {
                return minCompare;
            }

            return Double.compare( this.getMax().doubleValue(), o.getMax().doubleValue() );
        }
        else if( min instanceof Integer || min instanceof Long || min instanceof Short || min instanceof Byte ) {
            int minCompare = Long.compare( this.getMin().longValue(), o.getMin().longValue() );
            if ( minCompare != 0 ) {
                return minCompare;
            }

            return Long.compare( this.getMax().longValue(), o.getMax().longValue() );
        }
        else if( min instanceof BigInteger ) {
            int minCompare = ( (BigInteger)this.getMin() ).compareTo( (BigInteger)o.getMin() );
            if ( minCompare != 0 ) {
                return minCompare;
            }

            return ( (BigInteger)this.getMax() ).compareTo( (BigInteger)o.getMax() );
        }
        else if( min instanceof BigDecimal ) {
            int minCompare = ( (BigDecimal)this.getMin() ).compareTo( (BigDecimal)o.getMin() );
            if ( minCompare != 0 ) {
                return minCompare;
            }

            return ( (BigDecimal)this.getMax() ).compareTo( (BigDecimal)o.getMax() );
        }

        throw new IllegalArgumentException( "Unknown number to compare." );
    }
}
