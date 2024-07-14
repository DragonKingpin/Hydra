package com.pinecone.framework.unit.top;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.Collection;
import java.util.Iterator;

public interface Topper<E > extends PineUnit, Collection<E > {
    @Override
    int size();

    @Override
    boolean isEmpty();

    @Override
    void clear();

    @Override
    boolean add( E e );

    Collection<E > topmost();

    Topper<E > setTopmostSize( int nTopmost );

    int getTopmostSize();

    E nextEviction();

    boolean willAccept( E e );

    @Override
    default Iterator<E > iterator() {
        return this.topmost().iterator();
    }
}
