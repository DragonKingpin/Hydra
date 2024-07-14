package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.system.prototype.Prototype;
import com.pinecone.framework.system.prototype.TypeIndex;
import com.pinecone.framework.util.json.JSON;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LinkedTreeMap <K,V> extends TreeMap<K,V> implements PineUnit, ListedSortedMap<K, V >, Iterable<Map.Entry<K, V > > {
    protected static class LinkedEntry<K,V> extends TreeMap.Entry<K,V> {
        LinkedEntry<K,V> before, after;

        LinkedEntry( K key, V value, TreeMap.Entry<K,V > parent ) {
            super( key, value, parent );
        }

        public void extend( Map.Entry<K,V > entry ) {
            this.key   = entry.getKey();
            this.value = entry.getValue();
        }

        @Override
        public String toString() {
            return this.toJSONString();
        }

        @Override
        public String toJSONString() {
            return super.toJSONString();
        }

        @Override
        public TypeIndex prototype() {
            return Prototype.typeid( this );
        }
    }

    protected transient LinkedTreeMap.LinkedEntry<K,V> head;

    protected transient LinkedTreeMap.LinkedEntry<K,V> tail;

    protected final boolean accessOrder;


    // internal utilities

    private void linkNodeFirst( LinkedTreeMap.LinkedEntry<K,V> p ) {
        LinkedTreeMap.LinkedEntry<K,V> front = this.head;
        this.head = p;
        if ( front == null ) {
            this.tail = p;
        }
        else {
            p.after = front;
            front.before = p;
        }
    }

    private void linkNodeLast( LinkedTreeMap.LinkedEntry<K,V> p ) {
        LinkedTreeMap.LinkedEntry<K,V> last = this.tail;
        this.tail = p;
        if ( last == null ) {
            this.head = p;
        }
        else {
            p.before = last;
            last.after = p;
        }
    }

    private void linkBefore( LinkedTreeMap.LinkedEntry<K,V> newNode, LinkedTreeMap.LinkedEntry<K,V> succ ) {
        // assert succ != null;
        final LinkedEntry<K,V> pred = succ.before;
        //final LinkedEntry<K,V> newNode = new Node<>(pred, e, succ);
        newNode.before = pred;
        newNode.after  = succ;
        succ.before = newNode;
        if ( pred == null ) {
            this.head = newNode;
        }
        else {
            pred.after = newNode;
        }
    }

    private LinkedEntry<K,V> detachLastTailInsert() {
        LinkedEntry<K,V> lastInserted = LinkedTreeMap.this.tail;
        if( LinkedTreeMap.this.tail.before != null ){
            LinkedTreeMap.this.tail.before.after = null;
        }
        LinkedTreeMap.this.tail = LinkedTreeMap.this.tail.before;
        lastInserted.before = null;
        lastInserted.after = null;

        return lastInserted;
    }


    protected V putValFront( K key, V value, boolean onlyIfAbsent, boolean evict ) {
        TreeMap.Entry<K,V> t = this.root;
        if ( t == null ) {
            this.compare( key, key ); // type (and possibly null) check

            this.root = this.spawnNodeFront( key, value, null );
            this.size = 1;
            ++this.modCount;
            return null;
        }
        int cmp;
        TreeMap.Entry<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = this.comparator;

        TreeMap.Entry<K,V> legacy = null;
        if ( cpr != null ) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0) {
                    t = t.left;
                }
                else if ( cmp > 0 ) {
                    t = t.right;
                }
                else {
                    legacy = t;
                    break;
                }
            }
            while ( t != null );
        }
        else {
            if ( key == null ) {
                throw new NullPointerException();
            }
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if ( cmp < 0 ) {
                    t = t.left;
                }
                else if ( cmp > 0 ) {
                    t = t.right;
                }
                else {
                    legacy = t;
                    break;
                }
            }
            while ( t != null );
        }

        if ( legacy != null ) { // existing mapping for key
            V oldValue = legacy.value;
            if ( !onlyIfAbsent || oldValue == null ) {
                legacy.setValue( value );
            }
            this.afterNodeAccess( legacy );
            return oldValue;
        }

        TreeMap.Entry<K,V> e = this.spawnNodeFront( key, value, parent );
        if ( cmp < 0 ) {
            parent.left = e;
        }
        else {
            parent.right = e;
        }
        this.fixAfterInsertion(e);
        ++this.size;
        ++this.modCount;
        this.afterNodeInsertion( evict );
        return null;
    }

    @Override
    protected TreeMap.Entry<K,V > spawnNode( K key, V value, TreeMap.Entry<K,V > parent ) {
        LinkedTreeMap.LinkedEntry<K,V> p = new LinkedTreeMap.LinkedEntry<>( key, value, parent );
        this.linkNodeLast(p);
        return p;
    }

    protected TreeMap.Entry<K,V > spawnNodeFront( K key, V value, TreeMap.Entry<K,V > parent ) {
        LinkedTreeMap.LinkedEntry<K,V> p = new LinkedTreeMap.LinkedEntry<>( key, value, parent );
        this.linkNodeFirst(p);
        return p;
    }


    protected void unlinkFirst( LinkedEntry<K,V> f ) {
        f = (LinkedEntry<K,V>)this.onlyDeleteEntry( f );
        this.unlink( f );

//        // assert f == first && f != null;
//        final LinkedEntry<K,V> next = f.after;
//        f.after = null; // help GC
//        this.head = next;
//        if ( next == null ) {
//            this.tail = null;
//        }
//        else {
//            next.before = null;
//        }
    }

    private void unlinkLast( LinkedEntry<K,V> l ) {
        l = (LinkedEntry<K,V>)this.onlyDeleteEntry( l );
        this.unlink( l );
        // assert l == last && l != null;
//        final LinkedEntry<K,V> prev = l.before;
//        l.before = null; // help GC
//        this.tail = prev;
//        if ( prev == null ) {
//            this.head = null;
//        }
//        else {
//            prev.after = null;
//        }
    }

    protected void unlink( TreeMap.Entry<K,V> e ) {
        LinkedTreeMap.LinkedEntry<K,V> p = ( LinkedTreeMap.LinkedEntry<K,V> )e, b = p.before, a = p.after;
        p.before = p.after = null;
        if ( b == null ) {
            this.head = a;
        }
        else {
            b.after = a;
        }
        if ( a == null ) {
            this.tail = b;
        }
        else {
            a.before = b;
        }
    }

    @Override
    protected void afterNodeRemoval( TreeMap.Entry<K,V> e ) { // unlink
        this.unlink( e );
    }

    protected void afterNodeInsertion( boolean evict ) { // possibly remove eldest
        LinkedEntry<K,V> first;
        if ( evict && (first = this.head) != null && this.removeEldestEntry(first) ) {
            K key = first.key;
            TreeMap.Entry<K,V> candidate = getEntry( key );
            this.deleteEntry( candidate );
        }
    }

    protected void afterNodeAccess( TreeMap.Entry<K,V> e ) { // move node to last
        LinkedTreeMap.LinkedEntry<K,V> last;
        if ( this.accessOrder && (last = this.tail) != e ) {
            LinkedTreeMap.LinkedEntry<K,V> p = (LinkedTreeMap.LinkedEntry<K,V>)e, b = p.before, a = p.after;
            p.after = null;
            if ( b == null ) {
                this.head = a;
            }
            else {
                b.after = a;
            }
            if ( a != null ) {
                a.before = b;
            }
            else {
                last = b;
            }
            if ( last == null ) {
                this.head = p;
            }
            else {
                p.before = last;
                last.after = p;
            }
            this.tail = p;
            ++this.modCount;
        }
    }

    @Override
    protected void internalWriteEntries( ObjectOutputStream s ) throws IOException {
        for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
            s.writeObject(e.key);
            s.writeObject(e.value);
        }
    }

    @Override
    protected void internalReadEntries( int size, final ObjectInputStream s ) throws IOException, ClassNotFoundException {
        for ( int i = 0; i < size; i++ ) {
            @SuppressWarnings("unchecked")
            K key = (K) s.readObject();
            @SuppressWarnings("unchecked")
            V value = (V) s.readObject();
            this.putVal(  key, value, false, false );
        }
    }

    public LinkedTreeMap() {
        super();
        this.accessOrder = false;
    }

    public LinkedTreeMap( Comparator<? super K> comparator ) {
        this( comparator, false );
    }

    public LinkedTreeMap( boolean accessOrder ) {
        super();
        this.accessOrder = accessOrder;
    }

    public LinkedTreeMap( Comparator<? super K> comparator, boolean accessOrder ) {
        super( comparator );
        this.accessOrder = accessOrder;
    }

    public LinkedTreeMap( Map<? extends K, ? extends V> m ) {
        super();
        this.accessOrder = false;
        this.putMapEntries( m, false );
    }

    public LinkedTreeMap( SortedMap<K, ? extends V> m ) {
        super( m );
        this.accessOrder = false;
    }

    public boolean containsValue( Object value ) {
        for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
            V v = e.value;
            if ( v == value || (value != null && value.equals(v)) ) {
                return true;
            }
        }
        return false;
    }

    public V get( Object key ) {
        TreeMap.Entry<K,V> e;
        if ( (e = this.getEntry( key )) == null ) {
            return null;
        }
        if ( this.accessOrder ) {
            this.afterNodeAccess(e);
        }
        return e.value;
    }

    public V getOrDefault( Object key, V defaultValue ) {
        TreeMap.Entry<K,V> e;
        if ( (e = this.getEntry( key ) ) == null ) {
            return defaultValue;
        }
        if ( this.accessOrder ) {
            this.afterNodeAccess(e);
        }
        return super.getOrDefault( key, defaultValue );
    }

    public void clear() {
        super.clear();
        this.head = this.tail = null;
    }

    protected boolean removeEldestEntry( Map.Entry<K,V> eldest ) {
        return false;
    }

    // Linked & Deque operations
    public boolean contains( Object o ) {
        if( o instanceof Map.Entry<?, ? > ) {
            @SuppressWarnings("unchecked")
            Map.Entry<K,V > kv = (Map.Entry<K,V >) o;
            TreeMap.Entry<K,V > treeEntry = this.getEntry( kv.getKey() );
            return treeEntry != null && kv.getValue().equals( treeEntry.value );
        }
        return false;
    }

    public boolean add( Map.Entry<K,V > e ) {
        this.addLast( e );
        return true;
    }

    public boolean addAll( Collection<? extends Map.Entry<K, V> > c ) {
        if( c.size() == 0 ) {
            return false;
        }
        for ( Map.Entry<? extends K, ? extends V> e : c ) {
            this.addLast( e.getKey(), e.getValue() );
        }
        return true;
    }

    public V addFirst( K key, V value ) {
        return this.putValFront( key, value, false, true );
    }

    public V addLast( K key, V value ) {
        return this.put( key, value );
    }

    //@Override
    public void addFirst( Map.Entry<K,V > e ) {
        this.addFirst( e.getKey(), e.getValue() );
    }

    //@Override
    public void addLast( Map.Entry<K,V > e ) {
        this.addLast( e.getKey(), e.getValue() );
    }

    public Map.Entry<K,V > getFirst() {
        final Map.Entry<K,V > f = this.head;
        if ( f == null ) {
            throw new NoSuchElementException();
        }
        return f;
    }

    public Map.Entry<K,V > getLast() {
        final Map.Entry<K,V > l = this.tail;
        if ( l == null ) {
            throw new NoSuchElementException();
        }
        return l;
    }

    public Map.Entry<K,V > removeFirst() {
        final LinkedEntry<K,V > f = this.head;
        if ( f == null ) {
            throw new NoSuchElementException();
        }
        this.unlinkFirst( f );
        return f;
    }

    public Map.Entry<K,V > removeLast() {
        final LinkedEntry<K,V > l = this.tail;
        if ( l == null ) {
            throw new NoSuchElementException();
        }
        this.unlinkLast( l );
        return l;
    }

    // Queue operations.

    public Map.Entry<K,V > peek() {
        return this.head;
    }

    public Map.Entry<K,V > element() {
        return this.getFirst();
    }

    public Map.Entry<K,V > poll() {
        final LinkedEntry<K,V > f = this.head;
        if( f != null ) {
            this.unlinkFirst(f);
        }
        return f;
    }

    public Map.Entry<K,V > remove() {
        return this.removeFirst();
    }

    public boolean offer( Map.Entry<K,V > e ) {
        return this.add(e);
    }

    // Deque operations
    public boolean offerFirst( Map.Entry<K,V > e ) {
        this.addFirst( e );
        return true;
    }

    public boolean offerLast( Map.Entry<K,V > e ) {
        this.addLast(e);
        return true;
    }

    public Map.Entry<K,V > peekFirst() {
        return this.head;
    }

    public Map.Entry<K,V > peekLast() {
        return this.tail;
    }

    public Map.Entry<K,V > pollFirst() {
        final LinkedEntry<K,V > f = this.head;
        if( f != null ) {
            this.unlinkFirst(f);
        }
        return f;
    }

    public Map.Entry<K,V > pollLast() {
        final LinkedEntry<K,V > l = this.tail;
        if( l != null ) {
            this.unlinkLast(l);
        }
        return l;
    }

    public void push( Map.Entry<K,V > e ) {
        this.addFirst(e);
    }

    public Map.Entry<K,V > pop() {
        return this.removeFirst();
    }

    public boolean removeFirstOccurrence( Object o ) {
        if( o instanceof Map.Entry<?, ? > ) {
            @SuppressWarnings("unchecked")
            Map.Entry<K,V > kv = (Map.Entry<K,V >) o;
            TreeMap.Entry<K,V > treeEntry = this.getEntry( kv.getKey() );
            if( treeEntry != null && kv.getValue().equals( treeEntry.value ) ) {
                this.deleteEntry( treeEntry );
                return true;
            }
        }
        return false;
    }

    public boolean removeLastOccurrence( Object o ) {
        return this.removeFirstOccurrence( o ); // This is a map, all keys are unique.
    }

    private boolean isElementIndex( int index ) {
        return index >= 0 && index < this.size;
    }

    private boolean isPositionIndex( int index ) {
        return index >= 0 && index <= this.size;
    }

    private String outOfBoundsMsg( int index ) {
        return "Index: "+index+", Size: "+ this.size;
    }

    private void checkElementIndex( int index ) {
        if ( !this.isElementIndex(index) ) {
            throw new IndexOutOfBoundsException(this.outOfBoundsMsg(index));
        }
    }

    private void checkPositionIndex(int index) {
        if ( !this.isPositionIndex(index) ) {
            throw new IndexOutOfBoundsException(this.outOfBoundsMsg(index));
        }
    }

    public ListIterator<Map.Entry<K, V> > listIterator( int index ) {
        this.checkPositionIndex(index);
        return new LinkedListIterator( index );
    }

    public Iterator<Map.Entry<K, V> > iterator() {
        return new LinkedEntryIterator();
    }

    public Iterator<Map.Entry<K, V> > descendingIterator(){
        return new DescendingIterator();
    }

    public Object[] toArray() {
        Object[] result = new Object[ this.size ];
        int i = 0;
        for ( LinkedEntry<K, V > x = this.head; x != null; x = x.after ) {
            result[ i++ ] = x;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray( T[] a ) {
        if ( a.length < this.size ) {
            a = (T[])java.lang.reflect.Array.newInstance( a.getClass().getComponentType(), this.size );
        }
        int i = 0;
        Object[] result = a;
        for ( LinkedEntry<K, V > x = this.head; x != null; x = x.after ) {
            result[i++] = x;
        }

        if ( a.length > this.size ) {
            a[ this.size ] = null;
        }

        return a;
    }

    public boolean containsAll( Collection<?> c ) {
        for ( Object e : c ) {
            if ( !this.contains(e) ) {
                return false;
            }
        }
        return true;
    }

    public boolean removeAll( Collection<?> c ) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = this.iterator();
        while ( it.hasNext() ) {
            if ( c.contains(it.next()) ) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    public boolean retainAll( Collection<?> c ) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<Map.Entry<K,V > > it = this.iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Set<K > keySet() {
        Set<K> ks = this.keySet;
        if ( ks == null ) {
            ks = new LinkedKeySet();
            this.keySet = ks;
        }
        return ks;
    }

    final class LinkedKeySet extends AbstractSet<K> {
        public final int size()                 { return size; }

        public final void clear()               { LinkedTreeMap.this.clear(); }

        public final Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }

        public final boolean contains( Object o ) { return containsKey(o); }

        public final boolean remove( Object key ) {
            TreeMap.Entry<K,V> candidate = getEntry(  key );
            boolean b = candidate != null;
            deleteEntry( candidate );
            return b;
        }

        public final Spliterator<K> spliterator()  {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT );
        }

        public final void forEach( Consumer<? super K> action ) {
            if ( action == null ) {
                throw new NullPointerException();
            }
            int mc = modCount;
            for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
                action.accept(e.key);
            }

            if ( modCount != mc ) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = this.values;
        if ( vs == null ) {
            vs = new LinkedValues();
            this.values = vs;
        }
        return vs;
    }

    final class LinkedValues extends AbstractCollection<V> {
        public final int size()                 { return size; }

        public final void clear()               { LinkedTreeMap.this.clear(); }

        public final Iterator<V> iterator() {
            return new LinkedValueIterator();
        }

        public final boolean contains( Object o ) { return containsValue(o); }

        public final Spliterator<V> spliterator() {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED );
        }

        public final void forEach( Consumer<? super V> action ) {
            if (action == null) {
                throw new NullPointerException();
            }
            int mc = modCount;
            for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
                action.accept(e.value);
            }
            if ( modCount != mc ) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public Set<Map.Entry<K,V> > entrySet() {
        Set<Map.Entry<K,V>> es;
        return (es = this.entrySet) == null ? (this.entrySet = new LinkedEntrySet()) : es;
    }

    public Set<Map.Entry<K,V > > treeEntrySet() {
        return new EntrySet();
    }

    protected final class LinkedEntrySet extends AbstractSet<Map.Entry<K,V> > {
        public final int size()                 { return size; }

        public final void clear()               { LinkedTreeMap.this.clear(); }

        public final Iterator<Map.Entry<K,V> > iterator() {
            return new LinkedEntryIterator();
        }

        public final boolean contains( Object o ) {
            if ( !(o instanceof Map.Entry) ) {
                return false;
            }
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            Object key = e.getKey();
            TreeMap.Entry<K,V> candidate = getEntry(  key );
            return candidate != null && candidate.equals(e);
        }

        public final boolean remove( Object o ) {
            if ( o instanceof Map.Entry ) {
                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
                Object key = e.getKey();
                TreeMap.Entry<K,V> candidate = getEntry(  key );
                boolean b = candidate != null;
                deleteEntry( candidate );
                return b;
            }
            return false;
        }

        public final Spliterator<Map.Entry<K,V>> spliterator() {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT );
        }

        public final void forEach(Consumer<? super Map.Entry<K,V>> action) {
            if ( action == null ) {
                throw new NullPointerException();
            }
            int mc = modCount;
            for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
                action.accept(e);
            }
            if ( modCount != mc ) {
                throw new ConcurrentModificationException();
            }
        }
    }

    // Map overrides

    @Override
    public void forEach( BiConsumer<? super K, ? super V> action ) {
        if ( action == null ) {
            throw new NullPointerException();
        }
        int mc = modCount;
        for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
            action.accept(e.key, e.value);
        }
        if ( modCount != mc ) {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    public void replaceAll( BiFunction<? super K, ? super V, ? extends V> function ) {
        if ( function == null ) {
            throw new NullPointerException();
        }
        int mc = modCount;
        for ( LinkedTreeMap.LinkedEntry<K,V> e = head; e != null; e = e.after ) {
            e.value = function.apply(e.key, e.value);
        }
        if ( modCount != mc ) {
            throw new ConcurrentModificationException();
        }
    }

    // Iterators

    protected LinkedEntry<K, V > queryNodeByIndex( int index ) {
        // assert isElementIndex(index);

        if ( index < (this.size >> 1) ) {
            LinkedEntry<K, V > x = this.head;
            for ( int i = 0; i < index; i++ ) {
                x = x.after;
            }
            return x;
        }
        else {
            LinkedEntry<K, V > x = this.tail;
            for ( int i = size - 1; i > index; i-- ){
                x = x.before;
            }
            return x;
        }
    }

    protected abstract class LinkedTreeIterator {
        LinkedTreeMap.LinkedEntry<K,V> next;
        LinkedTreeMap.LinkedEntry<K,V> current;
        int expectedModCount;

        LinkedTreeIterator() {
            this.next = head;
            this.expectedModCount = modCount;
            this.current = null;
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        final LinkedTreeMap.LinkedEntry<K,V> nextNode() {
            LinkedTreeMap.LinkedEntry<K,V> e = next;
            if ( modCount != this.expectedModCount ) {
                throw new ConcurrentModificationException();
            }
            if ( e == null ) {
                throw new NoSuchElementException();
            }
            this.current = e;
            this.next = e.after;
            return e;
        }

        public final void remove() {
            TreeMap.Entry<K,V> p = this.current;
            if ( p == null ) {
                throw new IllegalStateException();
            }
            if ( modCount != this.expectedModCount ) {
                throw new ConcurrentModificationException();
            }

            this.current = null;
            deleteEntry( p );
            this.expectedModCount = modCount;
        }
    }

    protected final class LinkedKeyIterator extends LinkedTreeIterator implements Iterator<K> {
        public final K next() { return nextNode().getKey(); }
    }

    protected final class LinkedValueIterator extends LinkedTreeIterator implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }

    protected final class LinkedEntryIterator extends LinkedTreeIterator implements Iterator<Map.Entry<K,V>> {
        public final Map.Entry<K,V> next() { return nextNode(); }
    }

    protected class LinkedListIterator implements ListIterator<Map.Entry<K,V> > {
        private LinkedEntry<K,V> lastReturned;
        private LinkedEntry<K,V> next;
        private int nextIndex;
        private int expectedModCount = modCount;

        LinkedListIterator( int index ) {
            // assert isPositionIndex(index);
            next = (index == size) ? null : LinkedTreeMap.this.queryNodeByIndex( index );
            nextIndex = index;
        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public Map.Entry<K,V> next() {
            checkForComodification();
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.after;
            nextIndex++;
            return lastReturned;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public Map.Entry<K,V> previous() {
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null) ? tail : next.before;
            nextIndex--;
            return lastReturned;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            this.checkForComodification();
            if ( this.lastReturned == null ) {
                throw new IllegalStateException();
            }

            LinkedEntry<K,V> lastNext = this.lastReturned.after;
            deleteEntry( this.lastReturned );
            if ( this.next == this.lastReturned ) {
                this.next = lastNext;
            }
            else {
                this.nextIndex--;
            }
            this.lastReturned = null;
            this.expectedModCount++;
        }

        public void set( Map.Entry<K,V> e ) {
            if ( this.lastReturned == null ) {
                throw new IllegalStateException();
            }
            checkForComodification();
            this.lastReturned.extend( e );
        }

        public void add( Map.Entry<K,V> e ) {
            this.checkForComodification();
            this.lastReturned = null;
            if ( this.next == null ) {
                //linkLast(e);
                LinkedTreeMap.this.addLast( e );
            }
            else {
                LinkedTreeMap.this.addLast( e );
                LinkedEntry<K, V > lastInserted = LinkedTreeMap.this.detachLastTailInsert();
                LinkedTreeMap.this.linkBefore( lastInserted, next );
            }
            this.nextIndex++;
            this.expectedModCount++;
        }

        public void forEachRemaining( Consumer<? super Map.Entry<K,V>> action ) {
            Objects.requireNonNull(action);
            while ( modCount == this.expectedModCount && this.nextIndex < size ) {
                action.accept( this.next );
                this.lastReturned = this.next;
                this.next = this.next.after;
                this.nextIndex++;
            }
            this.checkForComodification();
        }

        final void checkForComodification() {
            if ( modCount != this.expectedModCount ) {
                throw new ConcurrentModificationException();
            }
        }
    }

    protected class DescendingIterator implements Iterator<Map.Entry<K,V> > {
        private final LinkedListIterator itr = new LinkedListIterator(size());

        public boolean hasNext() {
            return this.itr.hasPrevious();
        }

        public Map.Entry<K,V> next() {
            return this.itr.previous();
        }

        public void remove() {
            this.itr.remove();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        LinkedTreeMap<K,V > clone = (LinkedTreeMap<K,V> ) super.superClone();

        clone.head = null;
        clone.tail = null;
        clone.putMapEntries( this, false );

        return clone;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }

    public Deque<Map.Entry<K, V > > toQueue() {
        return new LinkedTreeMapList<>( this );
    }

    public List<Map.Entry<K, V > > toList() {
        return new LinkedTreeMapList<>( this );
    }
}
