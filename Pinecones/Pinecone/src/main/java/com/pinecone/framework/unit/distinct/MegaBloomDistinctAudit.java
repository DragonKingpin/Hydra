package com.pinecone.framework.unit.distinct;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class MegaBloomDistinctAudit<E > extends ArchBloomDistinctAudit<E > implements DistinctAudit<E > {
    protected List<BitSet >                  mBitSets      = new ArrayList<>() ;
    protected List<Map<Integer, Set<E > > >  mConflictMaps = new ArrayList<>() ;

    /**
     * Constructs
     *
     * @param bitSize              The size of the bit array used for the bloom filter.
     * @param iterators            A collection of iterators whose elements will be compared against the master prototype.
     * @param iteratorsCopy        A collection of iterators that serve as copies for auditing purposes.
     * @param distinctions         A collection to store the resulting distinctions found during the audit.
     * @param distinctType         The type of distinction to perform, either finding symmetric distinct elements or symmetric homogeneous elements.
     */
    public MegaBloomDistinctAudit( int bitSize, Collection<Iterator<E > > iterators, Collection<Iterator<E > > iteratorsCopy, Collection<E > distinctions, Class<? extends Map > conflictMapType, DistinctType distinctType ) {
        super( bitSize, iterators, iteratorsCopy, distinctions, conflictMapType, distinctType );
    }

    public MegaBloomDistinctAudit( int bitSize, Collection<Iterator<E > > iterators, Collection<Iterator<E > > iteratorsCopy, Collection<E > distinctions, DistinctType distinctType ) {
        this( bitSize, iterators, iteratorsCopy, distinctions, HashMap.class, distinctType );
    }

    public MegaBloomDistinctAudit( int bitSize, Collection<Iterator<E > > iterators, Collection<Iterator<E > > iteratorsCopy, DistinctType distinctType ) {
        this( bitSize, iterators, iteratorsCopy, new ArrayList<>(), distinctType );
    }

    public MegaBloomDistinctAudit( Collection<Iterator<E > > iterators, Collection<Iterator<E > > iteratorsCopy, DistinctType distinctType ) {
        this( (int)1e6, iterators, iteratorsCopy, distinctType );
    }

    public MegaBloomDistinctAudit( Collection<Collection<E > > collections, DistinctType distinctType, Collection<E > distinctions ) {
        this( (int)( DistinctAudit.getMaxSize( collections ) * (float)1.5 ), DistinctAudit.toIterators( collections ), DistinctAudit.toIterators( collections ), distinctions, HashMap.class, distinctType );
    }

    public MegaBloomDistinctAudit( int bitSize, Collection<Collection<E > > collections, DistinctType distinctType ) {
        this( bitSize, DistinctAudit.toIterators( collections ), DistinctAudit.toIterators( collections ), new ArrayList<>(), distinctType );
    }

    public MegaBloomDistinctAudit( Collection<Collection<E > > collections, DistinctType distinctType  ) {
        this( collections, distinctType, new ArrayList<>() );
    }


    @Override
    protected boolean hasOwnElement( int id, E element ) {
        int hash = element.hashCode();
        int index = Math.abs(hash % this.mBitSize);
        boolean owned = false;

        for ( int j = 0; j < this.mIteratorsCopy.size(); ++j ) {
            if ( id < 0 || id != j ) {
                BitSet                  bitmap = this.mBitSets.get(j);
                Map<Integer, Set<E > > hashMap = this.mConflictMaps.get(j);

                if ( bitmap.get(index) && hashMap.containsKey(index) && hashMap.get(index).contains(element) ) {
                    owned = true;
                    break;
                }
            }
        }

        return owned;
    }

    @Override
    public Collection<E > audit() {
        for ( int i = 0; i < this.mIterators.size(); ++i ) {
            this.mBitSets.add( new BitSet( this.mBitSize ) );
            this.mConflictMaps.add( this.newConflictMap() );
        }

        Iterator<Iterator<E > > iters = this.mIterators.iterator();
        int i = 0;
        while ( iters.hasNext() ) {
            Iterator<E >          iterator     = iters.next();
            BitSet                  bitset     = this.mBitSets.get(i);
            Map<Integer, Set<E > > conflictMap = this.mConflictMaps.get(i);

            this.addBitSet( iterator, bitset, conflictMap );

            ++i;
        }

        iters = this.mIteratorsCopy.iterator();
        i = 0;
        while ( iters.hasNext() ) {
            Iterator<E> iterator = iters.next();

            this.filterFromIterator( i, iterator );

            ++i;
        }


        return this.mDistinctions;
    }

    @Override
    public Collection<E> audit( Iterator<E> neoIter, Iterator<E> neoIterCopy ) {
        BitSet                  newBitset     = new BitSet( this.mBitSize );
        Map<Integer, Set<E > > newConflictMap = this.newConflictMap();

        this.addBitSet( neoIter, newBitset, newConflictMap );

        this.mBitSets.add( newBitset );
        this.mConflictMaps.add( newConflictMap );

        int id = this.mBitSets.size() - 1;
        while ( neoIterCopy.hasNext() ) {
            this.filterFromIterator( id, neoIterCopy );
        }

        return this.mDistinctions;
    }

}
