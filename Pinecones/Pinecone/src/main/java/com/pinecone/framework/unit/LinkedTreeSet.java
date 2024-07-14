package com.pinecone.framework.unit;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.*;

public class LinkedTreeSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {
    static final long serialVersionUID = -5024744406713321676L;

    private transient LinkedTreeMap<E,Object > map;

    private static final Object PRESENT = new Object();

    public LinkedTreeSet() {
        this.map = new LinkedTreeMap<>();
    }

    public LinkedTreeSet( Collection<? extends E> c ) {
        this.map = new LinkedTreeMap<>();
        this.addAll(c);
    }

    public LinkedTreeSet( Comparator<? super E> comparator ) {
        this.map = new LinkedTreeMap<>( comparator );
    }

    public LinkedTreeSet( Comparator<? super E> comparator, boolean accessOrder ) {
        super();
        this.map = new LinkedTreeMap<>( comparator, accessOrder );
    }

    public LinkedTreeSet( boolean accessOrder ) {
        super();
        this.map = new LinkedTreeMap<>( accessOrder );
    }


    public Iterator<E > iterator() {
        return this.map.keySet().iterator();
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean contains( Object o ) {
        return this.map.containsKey(o);
    }

    public boolean add(E e) {
        return this.map.put(e, PRESENT)==null;
    }

    public boolean remove(Object o) {
        return this.map.remove(o)==PRESENT;
    }

    public void clear() {
        this.map.clear();
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            LinkedTreeSet<E> newSet = (LinkedTreeSet<E>) super.clone();
            newSet.map = (LinkedTreeMap<E, Object > ) this.map.clone();
            return newSet;
        }
        catch ( CloneNotSupportedException e ) {
            throw new InternalError(e);
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();

        s.writeInt( this.map.size() );

        // Write out all elements in the proper order.
        for ( E e : this.map.keySet() ) {
            s.writeObject(e);
        }
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();

        int size = s.readInt();
        if ( size < 0 ) {
            throw new InvalidObjectException("Illegal size: " + size);
        }

        this.map = new LinkedTreeMap<>();

        for ( int i = 0; i < size; i++ ) {
            @SuppressWarnings("unchecked")
            E e = (E) s.readObject();
            this.map.put( e, PRESENT );
        }
    }

    public Spliterator<E> spliterator() {
        return LinkedTreeMap.keySpliteratorFor( this.map );
    }
}
