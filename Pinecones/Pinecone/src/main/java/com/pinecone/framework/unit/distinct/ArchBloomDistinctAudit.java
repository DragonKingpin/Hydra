package com.pinecone.framework.unit.distinct;

import com.pinecone.framework.unit.Units;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.Collection;

public abstract class ArchBloomDistinctAudit<E > implements DistinctAudit<E > {
    protected Collection<Iterator<E > >      mIterators                        ;
    protected Collection<Iterator<E > >      mIteratorsCopy                    ;
    protected int                            mBitSize                          ;
    protected Class<? extends Map >          mConflictMapType                  ;
    protected Collection<E >                 mDistinctions                     ;
    protected DistinctType                   mDistinctType                     ;


    protected ArchBloomDistinctAudit( int bitSize, Collection<Iterator<E > > iterators, Collection<Iterator<E > > iteratorsCopy, Collection<E > distinctions, Class<? extends Map > conflictMapType, DistinctType distinctType ) {
        this.mBitSize            = bitSize;
        this.mIterators          = iterators;
        this.mIteratorsCopy      = iteratorsCopy;
        this.mDistinctions       = distinctions;
        this.mConflictMapType    = conflictMapType;
        this.mDistinctType       = distinctType;
    }

    protected Map<Integer, Set<E > > newConflictMap() {
        return Units.newInstance( this.mConflictMapType );
    }

    @Override
    public boolean hasOwnElement( E element ) {
        return this.hasOwnElement( -1, element );
    }

    protected abstract boolean hasOwnElement( int id, E element );

    protected void filterFromIterator( int id, Iterator<E > iterator ){
        while ( iterator.hasNext() ) {
            E element = iterator.next();

            boolean owned = this.hasOwnElement( id, element );
            if ( this.mDistinctType == DistinctType.SymmetricDistinct && !owned ) {
                this.mDistinctions.add(element);
            }
            else if ( this.mDistinctType == DistinctType.SymmetricHomogeneity && owned ) {
                this.mDistinctions.add(element);
            }
        }
    }

    protected void addBitSet( Iterator<E > iterator, BitSet bitset, Map<Integer, Set<E > > conflictMap ) {
        while ( iterator.hasNext() ) {
            E element = iterator.next();
            int hash  = element.hashCode();
            int index = Math.abs( hash % this.mBitSize );

            if ( bitset.get( index ) ) {
                conflictMap.computeIfAbsent(index, k -> new HashSet<>()).add( element );
            }
            else {
                bitset.set( index );
                conflictMap.computeIfAbsent(index, k -> new HashSet<>()).add( element );
            }
        }
    }
}
