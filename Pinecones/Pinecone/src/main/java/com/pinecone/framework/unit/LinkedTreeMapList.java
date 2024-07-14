package com.pinecone.framework.unit;


import java.util.Map;
import java.util.AbstractSequentialList;
import java.util.List;
import java.util.Deque;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Iterator;

public class LinkedTreeMapList<K,V > extends AbstractSequentialList<Map.Entry<K, V > > implements List<Map.Entry<K, V > >, Deque<Map.Entry<K, V > > {
    static final long serialVersionUID = -5024789606714721619L;

    private transient LinkedTreeMap<K,V > map;

    public LinkedTreeMapList( LinkedTreeMap<K,V > map ) {
        super();
        this.map = map;
    }

    public LinkedTreeMapList() {
        super();
        this.map = new LinkedTreeMap<>();
    }

    public LinkedTreeMapList( Comparator<? super K> comparator ) {
        super();
        this.map = new LinkedTreeMap<>( comparator );
    }

    public LinkedTreeMapList( Comparator<? super K> comparator, boolean accessOrder ) {
        super();
        this.map = new LinkedTreeMap<>( comparator, accessOrder );
    }

    public LinkedTreeMapList( boolean accessOrder ) {
        super();
        this.map = new LinkedTreeMap<>( accessOrder );
    }

    public LinkedTreeMapList( Map<? extends K, ? extends V> m ) {
        super();
        this.map = new LinkedTreeMap<>( m );
    }

    public LinkedTreeMapList( SortedMap<K, ? extends V> m ) {
        super();
        this.map = new LinkedTreeMap<>( m );
    }

    public LinkedTreeMap<K,V > getMap() {
        return this.map;
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }



    public boolean remove(Object o) {
        return this.map.remove(o) != null;
    }

    public void clear() {
        this.map.clear();
    }



    // Linked & Deque operations

    public boolean contains( Object o ) {
        return this.map.contains(o);
    }

    public boolean add( Map.Entry<K, V > e ) {
        return this.map.put(e.getKey(), e.getValue())==null;
    }

    public boolean addAll( Collection<? extends Map.Entry<K, V> > c ) {
        return this.map.addAll( c );
    }

    public V addFirst( K key, V value ) {
        return this.map.addFirst( key, value );
    }

    public V addLast( K key, V value ) {
        return this.map.addLast( key, value );
    }

    //@Override
    public void addFirst( Map.Entry<K,V > e ) {
        this.map.addFirst( e );
    }

    //@Override
    public void addLast( Map.Entry<K,V > e ) {
        this.map.addLast( e );
    }

    public Map.Entry<K,V > getFirst() {
        return this.map.getFirst();
    }

    public Map.Entry<K,V > getLast() {
        return this.map.getLast();
    }

    public Map.Entry<K,V > removeFirst() {
        return this.map.removeFirst();
    }

    public Map.Entry<K,V > removeLast() {
        return this.map.removeLast();
    }

    // Queue operations.

    public Map.Entry<K,V > peek() {
        return this.map.peek();
    }

    public Map.Entry<K,V > element() {
        return this.map.element();
    }

    public Map.Entry<K,V > poll() {
        return this.map.poll();
    }

    public Map.Entry<K,V > remove() {
        return this.map.remove();
    }

    public boolean offer( Map.Entry<K,V > e ) {
        return this.map.offer(e);
    }

    // Deque operations
    public boolean offerFirst( Map.Entry<K,V > e ) {
        return this.map.offerFirst(e);
    }

    public boolean offerLast( Map.Entry<K,V > e ) {
        return this.map.offerLast(e);
    }

    public Map.Entry<K,V > peekFirst() {
        return this.map.peekFirst();
    }

    public Map.Entry<K,V > peekLast() {
        return this.map.peekLast();
    }

    public Map.Entry<K,V > pollFirst() {
        return this.map.pollFirst();
    }

    public Map.Entry<K,V > pollLast() {
        return this.map.pollLast();
    }

    public void push( Map.Entry<K,V > e ) {
        this.map.push(e);
    }

    public Map.Entry<K,V > pop() {
        return this.map.pop();
    }

    public boolean removeFirstOccurrence( Object o ) {
        return this.map.removeFirstOccurrence(o);
    }

    public boolean removeLastOccurrence( Object o ) {
        return this.map.removeLastOccurrence(o);
    }


    public ListIterator<Map.Entry<K, V> > listIterator( int index ) {
        return this.map.listIterator( index );
    }

    public Iterator<Map.Entry<K, V> > iterator() {
        return this.map.iterator();
    }

    public Iterator<Map.Entry<K, V> > descendingIterator(){
        return this.map.descendingIterator();
    }

    public Object[] toArray() {
        return this.map.toArray();
    }

    public <T> T[] toArray( T[] a ) {
        return this.map.toArray(a);
    }

    public boolean containsAll( Collection<?> c ) {
        return this.map.containsAll(c);
    }

    public boolean removeAll( Collection<?> c ) {
        return this.map.removeAll(c);
    }

    public boolean retainAll( Collection<?> c ) {
        return this.map.retainAll(c);
    }

}
