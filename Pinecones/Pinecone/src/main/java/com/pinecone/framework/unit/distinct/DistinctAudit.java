package com.pinecone.framework.unit.distinct;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface DistinctAudit<E > extends Pinenut {
    boolean hasOwnElement( E element );

    Collection<E > audit();

    Collection<E> audit( Iterator<E> neoIter, Iterator<E> neoIterCopy );

    default Collection<E> audit( Collection<E> neo ) {
        return this.audit( neo.iterator(), neo.iterator() );
    }

    static <E> Collection<Iterator<E>> toIterators( Collection<Collection<E > > collections ) {
        List<Iterator<E>> iterators = new ArrayList<>();
        for ( Collection<E> collection : collections ) {
            iterators.add(collection.iterator());
        }
        return iterators;
    }

    static <E> int getMaxSize( Collection<Collection<E>> collections ) {
        int maxSize = 0;
        for ( Collection<E> collection : collections ) {
            maxSize = Math.max( maxSize, collection.size() );
        }
        return maxSize;
    }
}
