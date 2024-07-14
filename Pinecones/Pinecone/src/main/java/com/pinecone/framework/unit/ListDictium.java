package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.system.prototype.Prototype;
import com.pinecone.framework.system.prototype.TypeIndex;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSON;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.NoSuchElementException;

public class ListDictium<V > implements Dictium<V >, List<V > {
    private List<V > mTargetList;

    public ListDictium( List<V > target ) {
        this.mTargetList = target;
    }

    public ListDictium() {
        this( new ArrayList<>() );
    }

    public static int asInt32Key( Object key ) {
        if ( key instanceof Integer ) {
            return (int) key;
        }
        else if ( key instanceof Float || key instanceof Double || key instanceof BigDecimal ) {
            throw new IllegalArgumentException( "Array does not allow float as key." );
        }
        else if ( key instanceof Number ) {
            return ((Number) key).intValue();
        }
        else if ( key instanceof String ) {
            return Integer.parseInt((String) key);
        }

        throw new IllegalArgumentException( "Key of Array should be integer or integer-fmt-string." );
    }

    @Override
    public int size() {
        return this.mTargetList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mTargetList.isEmpty();
    }

    @Override
    public void clear() {
        this.mTargetList.clear();
    }

    @Override
    public boolean containsKey( Object key ) {
        try {
            int index = ListDictium.asInt32Key( key );
            return index >= 0 && index < this.mTargetList.size();
        }
        catch ( IllegalArgumentException e ) {
            return false;
        }
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.contains( value );
    }

    @Override
    public boolean contains( Object o ) {
        return this.mTargetList.contains( o );
    }

    @Override
    public boolean add( V v ) {
        return this.mTargetList.add( v );
    }

    @Override
    public boolean containsAll( Collection<?> c ) {
        return this.mTargetList.containsAll( c );
    }

    @Override
    public boolean addAll( int index, Collection<? extends V> c ) {
        return this.mTargetList.addAll( index, c );
    }

    @Override
    public boolean addAll( Collection<? extends V> c ) {
        return this.mTargetList.addAll( c );
    }

    @Override
    public boolean removeAll( Collection<?> c ) {
        return this.mTargetList.removeAll( c );
    }

    @Override
    public boolean retainAll( Collection<?> c ) {
        return this.mTargetList.retainAll( c );
    }

    @Override
    public V get( Object key ) {
        try {
            int index = ListDictium.asInt32Key( key );
            if ( index >= 0 && index < this.mTargetList.size() ) {
                return this.mTargetList.get( index );
            }
        }
        catch ( IllegalArgumentException e ) {
            // Do nothing
        }
        return null;
    }

    @Override
    public V get( int index ) {
        return this.mTargetList.get( index );
    }

    @Override
    public V set( int index, V value ) {
        while ( this.mTargetList.size() <= index ) {
            this.mTargetList.add( null );
        }
        return this.mTargetList.set( index, value );
    }

    @Override
    public void add( int index, V value ) {
        while ( this.mTargetList.size() <= index ) {
            this.mTargetList.add( null );
        }
        this.mTargetList.add( index, value );
    }


    @Override
    public V insert( Object key, V value ) {
        int index = ListDictium.asInt32Key( key );
        return this.set( index, value );
    }

    @Override
    public V insertIfAbsent( Object key, V value ) {
        if( !this.containsKey( ListDictium.asInt32Key( key ) ) ){
            this.insert( key, value );
        }
        return null;
    }

    @Override
    public V erase( Object key ) {
        try {
            int index = ListDictium.asInt32Key( key );
            if ( index >= 0 && index < this.mTargetList.size() ) {
                return this.mTargetList.remove(index);
            }
        }
        catch ( IllegalArgumentException e ) {
            // Do nothing
        }
        return null;
    }

    @Override
    public boolean remove( Object key ) {
        this.erase( key );
        return true;
    }

    @Override
    public V remove( int index ) {
        return this.mTargetList.remove( index );
    }

    @Override
    public int indexOf( Object o ) {
        return this.mTargetList.indexOf( o );
    }

    @Override
    public int lastIndexOf( Object o ) {
        return this.mTargetList.lastIndexOf( o );
    }

    @Override
    public ListIterator<V> listIterator() {
        return this.mTargetList.listIterator();
    }

    @Override
    public ListIterator<V> listIterator( int index ) {
        return this.mTargetList.listIterator( index );
    }

    @Override
    public List<V> subList( int fromIndex, int toIndex ) {
        return this.mTargetList.subList( fromIndex, toIndex );
    }

    @Override
    public Iterator<V > iterator() {
        return this.mTargetList.iterator();
    }

    @Override
    public Set<Map.Entry<Integer, V > > entrySet() {
        return new ListEntrySet();
    }

    public Set<Integer > keySet() {
        return new ListKeyEntrySet();
    }

    @Override
    public Collection<V > values() {
        return this.mTargetList;
    }

    @Override
    public Map<Integer, V > toMap() {
        return new ListMap();
    }

    @Override
    public List<V > toList() {
        return this.mTargetList;
    }

    @Override
    public boolean hasOwnProperty( Object index ) {
        return this.containsKey( index );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }

    @Override
    public <T> T[] toArray( T[] a ) {
        return this.mTargetList.<T>toArray( a );
    }

    @Override
    public Object[] toArray() {
        return this.mTargetList.toArray();
    }


    protected class ListEntrySet extends AbstractSet<Map.Entry<Integer, V > > {
        @Override
        public Iterator<Map.Entry<Integer, V > > iterator() {
            return new ListEntryIterator();
        }

        @Override
        public int size() {
            return ListDictium.this.mTargetList.size();
        }

        @Override
        public void clear() {
            ListDictium.this.clear();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            Object key = e.getKey();
            Object v = ListDictium.this.get(key);
            return v != null && v.equals(e.getValue());
        }

        @Override
        public boolean remove(Object o) {
            if (this.contains(o)) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                Object key = e.getKey();
                return ListDictium.this.erase(key) != null;
            }
            return false;
        }

        @Override
        public Spliterator<Map.Entry<Integer, V>> spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT);
        }
    }

    protected abstract class DiListEntryIterator {
        protected Iterator<V> currentIterator;
        protected int index;
        protected ListEntry<V> dummyEntry;

        public DiListEntryIterator() {
            this.index = 0;
            this.currentIterator = ListDictium.this.mTargetList.iterator();
            this.dummyEntry = new ListEntry<>( this.index, null );
        }

        public boolean hasNext() {
            return this.currentIterator.hasNext();
        }

        protected Map.Entry<Integer, V> nextNode() {
            if ( !this.hasNext() ) {
                throw new NoSuchElementException();
            }

            this.dummyEntry.setKey( this.index++ );
            this.dummyEntry.setValue( this.currentIterator.next() );
            return this.dummyEntry;
        }

        public void remove() {
            this.currentIterator.remove();
        }
    }

    protected class ListEntryIterator extends DiListEntryIterator implements Iterator<Map.Entry<Integer, V>> {
        @Override
        public Map.Entry<Integer, V> next() {
            return this.nextNode();
        }
    }

    protected static boolean valEquals( Object o1, Object o2 ) {
        return (o1==null ? o2==null : o1.equals(o2));
    }

    protected static class ListEntry<V > implements Map.Entry<Integer, V >, Pinenut {
        Integer key;
        V  value;

        ListEntry( Integer key, V value ) {
            this.key   = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue( V value ) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public void setKey( Integer key ) {
            this.key = key;
        }

        @Override
        public boolean equals( Object o ) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;

            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        @Override
        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        @Override
        public String toString() {
            return this.toJSONString();
        }

        @Override
        public String toJSONString() {
            return "{" + StringUtils.jsonQuote( this.key.toString() ) + ":" + JSON.stringify( this.value ) + "}";
        }

        @Override
        public TypeIndex prototype() {
            return Prototype.typeid( this );
        }
    }




    protected class ListKeyIterator extends DiListEntryIterator implements Iterator<Integer > {
        @Override
        public Integer next() {
            return this.nextNode().getKey();
        }
    }

    protected class ListKeyEntrySet extends AbstractSet<Integer > {
        @Override
        public Iterator<Integer > iterator() {
            return new ListKeyIterator();
        }

        @Override
        public int size() {
            return ListDictium.this.mTargetList.size();
        }

        @Override
        public void clear() {
            ListDictium.this.clear();
        }

        @Override
        public boolean contains( Object o ) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            Object key = e.getKey();
            Object v = ListDictium.this.get(key);
            return v != null && v.equals(e.getValue());
        }

        @Override
        public boolean remove( Object o ) {
            if (this.contains(o)) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                Object key = e.getKey();
                return ListDictium.this.erase(key) != null;
            }
            return false;
        }

        @Override
        public Spliterator<Integer > spliterator() {
            return Spliterators.spliterator(this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT);
        }
    }

    protected class ListMap implements Map<Integer, V >, PineUnit {
        @Override
        public int size() {
            return ListDictium.this.size();
        }

        @Override
        public boolean isEmpty() {
            return ListDictium.this.isEmpty();
        }

        @Override
        public void clear() {
            ListDictium.this.clear();
        }

        @Override
        public boolean containsKey( Object key ) {
            return ListDictium.this.containsKey( key );
        }

        @Override
        public boolean containsValue( Object value ) {
            return ListDictium.this.containsValue( value );
        }

        @Override
        public V get( Object key ) {
            return ListDictium.this.get( key );
        }

        @Override
        public V put( Integer index, V value ) {
            return ListDictium.this.set( index, value );
        }

        @Override
        public V remove( Object key ) {
            return ListDictium.this.erase( key );
        }

        @Override
        public void putAll( Map<? extends Integer, ? extends V> m ) {
            for( Map.Entry<? extends Integer, ? extends V > kv: m.entrySet() ) {
                this.put( kv.getKey(), kv.getValue() );
            }
        }

        @Override
        public Set<Map.Entry<Integer, V > > entrySet() {
            return new ListEntrySet();
        }

        @Override
        public Set<Integer > keySet() {
            return new ListKeyEntrySet();
        }

        @Override
        public Collection<V > values() {
            return ListDictium.this.values();
        }

        @Override
        public boolean hasOwnProperty( Object index ) {
            return this.containsKey( index );
        }

        @Override
        public String toString() {
            return this.toJSONString();
        }

        @Override
        public String toJSONString() {
            return JSON.stringify( this );
        }
    }
}
