package com.pinecone.framework.unit.distinct;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class MegaPrototypeBloomDistinctAudit<E > extends ArchBloomDistinctAudit<E > implements DistinctAudit<E > {
    protected Iterator<E >           mMasterProtoIterator;
    protected BitSet                 mMasterBitSet;
    protected Map<Integer, Set<E > > mMasterConflictMap;

    public MegaPrototypeBloomDistinctAudit(
            int bitSize, Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, Collection<Iterator<E>> iteratorsCopy,
            Collection<E> distinctions, Class<? extends Map> conflictMapType, DistinctType distinctType
    ) {
        super( bitSize, iterators, iteratorsCopy, distinctions, conflictMapType, distinctType );
        this.mMasterProtoIterator = masterProtoIterator;
        this.mMasterBitSet        = new BitSet(bitSize);
        this.mMasterConflictMap   = this.newConflictMap();

        this.addBitSet( this.mMasterProtoIterator, this.mMasterBitSet, this.mMasterConflictMap );
    }

    /**
     * Constructs
     *
     * @param bitSize              The size of the bit array used for the bloom filter.
     * @param masterProtoIterator  The master prototype iterator that will be used as the reference for comparison.
     * @param iterators            A collection of iterators whose elements will be compared against the master prototype.
     * @param iteratorsCopy        A collection of iterators that serve as copies for auditing purposes.
     * @param distinctions         A collection to store the resulting distinctions found during the audit.
     * @param distinctType         The type of distinction to perform, either finding symmetric distinct elements or symmetric homogeneous elements.
     */
    public MegaPrototypeBloomDistinctAudit( int bitSize, Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, Collection<Iterator<E>> iteratorsCopy, Collection<E> distinctions, DistinctType distinctType ) {
        this( bitSize, masterProtoIterator, iterators, iteratorsCopy, distinctions, HashMap.class, distinctType);
    }

    public MegaPrototypeBloomDistinctAudit( int bitSize, Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, Collection<Iterator<E>> iteratorsCopy, DistinctType distinctType ) {
        this( bitSize, masterProtoIterator, iterators, iteratorsCopy, new ArrayList<>(), distinctType);
    }

    public MegaPrototypeBloomDistinctAudit( Iterator<E> masterProtoIterator, Collection<Iterator<E>> iterators, Collection<Iterator<E>> iteratorsCopy, DistinctType distinctType ) {
        this( (int) 1e6, masterProtoIterator, iterators, iteratorsCopy, distinctType );
    }

    public MegaPrototypeBloomDistinctAudit( int bitSize, Iterator<E> masterProtoIterator, Collection<Collection<E>> collections, DistinctType distinctType ) {
        this( bitSize, masterProtoIterator, DistinctAudit.toIterators(collections), DistinctAudit.toIterators(collections), new ArrayList<>(), HashMap.class, distinctType);
    }

    public MegaPrototypeBloomDistinctAudit( int bitSize, Iterator<E> masterProtoIterator, Collection<Collection<E>> collections, DistinctType distinctType, Collection<E> distinctions ) {
        this( bitSize, masterProtoIterator, DistinctAudit.toIterators(collections), DistinctAudit.toIterators(collections), distinctions, HashMap.class, distinctType);
    }

    public MegaPrototypeBloomDistinctAudit( Iterator<E> masterProtoIterator, Collection<Collection<E>> collections, DistinctType distinctType, Collection<E> distinctions ) {
        this( (int) (DistinctAudit.getMaxSize(collections) * 1.5), masterProtoIterator, collections, distinctType, distinctions );
    }

    public MegaPrototypeBloomDistinctAudit( Iterator<E> masterProtoIterator, Collection<Collection<E>> collections, DistinctType distinctType ) {
        this( (int) (DistinctAudit.getMaxSize(collections) * 1.5), masterProtoIterator, collections, distinctType );
    }


    @Override
    protected boolean hasOwnElement( int id, E element ) {
        int  hash = element.hashCode();
        int index = Math.abs( hash % this.mBitSize );
        return this.mMasterBitSet.get(index) && this.mMasterConflictMap.containsKey(index) && this.mMasterConflictMap.get(index).contains(element);
    }


    @Override
    public Collection<E > audit() {
        Iterator<Iterator<E > > iters = this.mIteratorsCopy.iterator();
        int i = 0;
        while ( iters.hasNext() ) {
            this.filterFromIterator( i, iters.next() );
            ++i;
        }

        return this.mDistinctions;
    }

    @Override
    public Collection<E> audit( Iterator<E> neoIter, Iterator<E> neoIterCopy ) {
        BitSet                  newBitset     = new BitSet( this.mBitSize );
        Map<Integer, Set<E > > newConflictMap = this.newConflictMap();

        this.addBitSet( neoIter, newBitset, newConflictMap );

        this.filterFromIterator( -1, neoIterCopy );

        return this.mDistinctions;
    }
}
