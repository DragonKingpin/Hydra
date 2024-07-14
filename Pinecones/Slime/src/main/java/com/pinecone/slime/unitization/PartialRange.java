package com.pinecone.slime.unitization;

public interface PartialRange<T extends Comparable<T > > extends Range, Comparable<PartialRange<T > > {
    T getMin();

    T getMax();

    void   setRange( T min, T max );

    void   setMin  ( T min );

    void   setMax  ( T max );

    @Override
    default String toJSONString() {
        return String.format(
                "{\"class\":\"%s\",\"min\":%s,\"max\":%s}", this.className(), this.getMin(), this.getMax()
        );
    }

    @Override
    default boolean contains( Object elm ) {
        if ( elm == null ) {
            return false;
        }

        if ( !( elm instanceof Comparable ) ) {
            throw new ClassCastException( "Element is not comparable." );
        }

        @SuppressWarnings( "unchecked" )
        Comparable<T> comparableElm = (Comparable<T>) elm;

        return ( comparableElm.compareTo((T) this.getMin() ) >= 0 ) && ( comparableElm.compareTo((T) this.getMax()) <= 0 ); // [min, max]
    }

    @Override
    default int compareTo( PartialRange<T > o ) {
        int minCompare = this.getMin().compareTo( o.getMin() );
        if ( minCompare != 0 ) {
            return minCompare;
        }
        return this.getMax().compareTo( o.getMax() );
    }


    default int compareTo( T that ) {
        if( this.contains( that ) ) {
            return 0;
        }
        else if ( this.getMin().compareTo( that ) > 0 ) { // this > that
            return 1;
        }
        else if ( this.getMax().compareTo( that ) < 0 ) { // this < that
            return -1;
        }
        else {
            return 0; // Jesus!
        }
    }


    IntervalRangeComparator DefaultIntervalRangeComparator = new IntervalRangeComparator();
}
