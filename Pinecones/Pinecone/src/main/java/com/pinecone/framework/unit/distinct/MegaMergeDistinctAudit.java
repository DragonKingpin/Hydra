package com.pinecone.framework.unit.distinct;
import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.unit.Units;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

/**
 * MegaMergeDistinctAudit
 * These two iterators should each include unique elements.
 * @param <E>
 */
public class MegaMergeDistinctAudit<E > implements DistinctAudit<E > {
    protected Iterator<E >   mIterator1;
    protected Iterator<E >   mIterator2;
    protected int            mSegmentSize;
    protected Set<E >        mDistinctSet;
    protected Class<? >      mSetType;
    protected Set<E>         mResultSet;

    protected Set<E > newSet() {
        return Units.newInstance( this.mSetType );
    }

    protected Set<E > newSet( Object...args ) {
        return Units.newInstance( this.mSetType, args );
    }


    public MegaMergeDistinctAudit( Iterator<E> iterator1, Iterator<E> iterator2, int segmentSize, Class<? > setType ) {
        this.mIterator1     = iterator1;
        this.mIterator2     = iterator2;
        this.mSegmentSize   = segmentSize;
        this.mSetType       = setType;
        this.mDistinctSet   = this.newSet();
        this.mResultSet     = this.newSet();
    }

    public MegaMergeDistinctAudit( Iterator<E> iterator1, Iterator<E> iterator2, int segmentSize ) {
        this( iterator1, iterator2, segmentSize, HashSet.class );
    }

    @Override
    public Collection<E > audit() {
        while ( this.mIterator1.hasNext() || this.mIterator2.hasNext() ) {
            Set<E > segment1 = this.getNextSegment( this.mIterator1, this.mSegmentSize );
            Set<E > segment2 = this.getNextSegment( this.mIterator2, this.mSegmentSize );

            Set<E > processedSegment = this.xorSets( segment1, segment2 );
            this.mResultSet = this.mergeResults( this.mResultSet, processedSegment );
        }
        return this.mResultSet;
    }

    protected Set<E > getNextSegment( Iterator<E> iterator, int segmentSize ) {
        Set<E > segment = this.newSet();
        int count = 0;
        while ( iterator.hasNext() && count < segmentSize ) {
            segment.add( iterator.next() );
            count++;
        }
        return segment;
    }

    protected Set<E> xorSets( Set<E> set1, Set<E> set2 ) {
        Set<E > result = this.newSet(set1);
        for ( E element : set2 ) {
            if ( !result.add(element) ) {
                result.remove(element);
            }
        }
        return result;
    }

    protected Set<E> mergeResults( Set<E> resultSet, Set<E> processedSegment ) {
        return this.xorSets( resultSet, processedSegment );
    }

    @Override
    public boolean hasOwnElement( E element ) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<E> audit( Iterator<E> neoIter, @Nullable Iterator<E> dummy ) {
        throw new NotImplementedException();
    }
}
