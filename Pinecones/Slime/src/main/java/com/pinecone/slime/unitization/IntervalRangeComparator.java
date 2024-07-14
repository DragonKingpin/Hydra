package com.pinecone.slime.unitization;

import java.util.Comparator;

public class IntervalRangeComparator implements Comparator<Object > {
    @Override
    @SuppressWarnings( "unchecked" )
    public int compare( Object o1, Object o2 ) {
        if( o1 instanceof PartialRange && o2 instanceof PartialRange ) {
            return ((PartialRange) o1).compareTo( (PartialRange)o2 );
        }
        else if ( o1 instanceof PartialRange && o2 instanceof Comparable ) {
            PartialRange range = (PartialRange) o1;
            return range.compareTo( (Comparable)o2 );
        }
        else if ( o1 instanceof Comparable && o2 instanceof PartialRange ) {
            return -this.compare( o2, o1 );
        }
        else {
            throw new IllegalArgumentException( "Objects are not of type PartialRange or Comparable" );
        }
    }
}
