package com.pinecone.framework.unit.top;

import com.pinecone.framework.util.json.JSON;

import java.util.AbstractCollection;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collection;
import java.util.Iterator;

public class HeapTopper<E > extends AbstractCollection<E > implements Topper<E > {
    private int                          mnTopmost;
    private final PriorityQueue<E>       mHeap;
    private final Comparator<? super E>  mComparator;

    public HeapTopper( int nTopmost, Comparator<? super E> comparator ) {
        this.mnTopmost    = nTopmost;
        this.mComparator  = comparator;
        this.mHeap        = new PriorityQueue<>( nTopmost, comparator );
    }

    public HeapTopper( int nTopmost ) {
        this( nTopmost, new Comparator<E>() {
            @Override
            @SuppressWarnings( "unchecked" )
            public int compare( E o1, E o2 ) {
                return ( (Comparable<E >)o1 ).compareTo( o2 );
            }
        } );
    }

    @Override
    public int size() {
        return this.mHeap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mHeap.isEmpty();
    }

    @Override
    public void clear() {
        this.mHeap.clear();
    }

    @Override
    public boolean add( E e ) {
        if ( this.mHeap.size() < this.mnTopmost ) {
            this.mHeap.offer(e);
        }
        else if ( this.mComparator.compare( e, this.mHeap.peek() ) > 0 ) {
            this.mHeap.poll();
            this.mHeap.offer(e);
        }
        return true;
    }

    @Override
    public boolean addAll( Collection<? extends E> c ) {
        for( E e : c ) {
            this.add( e );
        }
        return true;
    }

    @Override
    public boolean removeAll( Collection<?> c ) {
        return this.mHeap.removeAll( c );
    }

    @Override
    public boolean retainAll( Collection<?> c ) {
        return this.mHeap.retainAll( c );
    }

    @Override
    public boolean remove( Object o ) {
        return this.mHeap.remove( o );
    }

    @Override
    public Collection<E > topmost() {
        return this.mHeap;
    }

    @Override
    public Topper<E > setTopmostSize( int nTopmost ) {
        this.mnTopmost = nTopmost;
        while ( this.mHeap.size() > nTopmost ) {
            this.mHeap.poll();
        }
        return this;
    }

    @Override
    public int getTopmostSize() {
        return this.mnTopmost;
    }

    @Override
    public E nextEviction() {
        return this.mHeap.peek();
    }

    @Override
    public boolean willAccept( E e ) {
        return this.mHeap.size() < this.mnTopmost || this.mComparator.compare( e, this.mHeap.peek() ) > 0;
    }

    @Override
    public boolean containsKey( Object key ) {
        if( key instanceof Number ) {
            int i = ((Number) key).intValue();
            return this.getTopmostSize() > i;
        }
        return false;
    }

    @Override
    public boolean containsAll( Collection<?> c ) {
        return this.mHeap.containsAll( c );
    }

    @Override
    public boolean contains( Object o ) {
        return this.topmost().contains( o );
    }

    @Override
    public boolean hasOwnProperty( Object elm ) {
        return this.contains( elm );
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public Iterator<E > iterator() {
        return this.topmost().iterator();
    }
}