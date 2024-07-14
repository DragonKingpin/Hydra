package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.json.JSON;

import java.io.Serializable;
import java.util.*;

public class UniScopeMaptron<K, V > implements PineUnit, Map<K, V >, UniScopeMap<K, V >, Cloneable, Serializable, Iterable<Map.Entry<K, V > > {
    protected UniScopeMap<K, V >    mParent;   // This is the [[prototype]] link, same as Javascript.

    protected Map<K, V>             mThisMap;

    protected transient Set<Map.Entry<K,V> > entrySet;
    protected transient Set<K>               scKeySet;
    protected transient Collection<V>        scValues;

    public UniScopeMaptron() {
        this( true, null );
    }

    public UniScopeMaptron( Map<K, V > thisMap, UniScopeMap prototype ){
        this.mThisMap = thisMap;
        this.mParent  = prototype;
    }

    public UniScopeMaptron( boolean bLinked, UniScopeMap prototype ){
        this( bLinked ? new LinkedHashMap<>() : new HashMap<>(), prototype );
    }

    public UniScopeMaptron( Map<K, V > thisMap ){
        this( thisMap, null );
    }

    /** Scope Map **/
    @Override
    public UniScopeMap<K, V > parent() {
        return this.mParent;
    }

    @Override
    public Map<K, V >         thisScope(){
        return this.mThisMap;
    }

    @Override
    public UniScopeMap<K, V > setParent    ( UniScopeMap<K, V > that ) {
        this.mParent = that;
        return this;
    }

    @Override
    public UniScopeMap<K, V > setThisScope ( Map<K, V > that ) {
        this.mThisMap = that;
        return this;
    }

    @Override
    public ScopeMap<K, V >    elevate      ( Map<K, V > child ) {
        UniScopeMaptron<K, V > sup = new UniScopeMaptron<>( this.mThisMap, this.mParent );
        this.setThisScope( child );
        this.setParent   ( sup   );
        return this;
    }

    /** Basic Map **/
    @Override
    public int size() {
        return this.mThisMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mThisMap.isEmpty();
    }

    @Override
    public boolean containsKey( Object key ) {
        boolean result = this.mThisMap.containsKey(key);
        if ( !result && this.mParent != null ) {
            result = this.mParent.containsKey( key );
        }

        return result;
    }

    @Override
    public boolean containsValue( Object value ) {
        boolean result = this.mThisMap.containsValue(value);
        if ( !result && this.mParent != null ) {
            result = this.mParent.containsValue( value );
        }

        return result;
    }

    @Override
    public V get( Object key ) {
        V val = this.mThisMap.get( key );
        if ( val == null && this.mParent != null ) {
            val = this.mParent.get( key );
        }

        return val;
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        this.mThisMap.putAll( m );
    }

    public UniScopeMaptron xPutAll(Map<? extends K, ? extends V> m ) {
        this.putAll(m);
        return this;
    }

    @Override
    public void clear() {
        this.mThisMap.clear();
    }

    public UniScopeMaptron xClear() {
        this.clear();
        return this;
    }

    @Override
    public V remove( Object key ) {
        V v = this.mThisMap.remove(key);
        if( v == null && this.mParent != null ) {
            v = this.mParent.remove( key );
        }

        return v;
    }

    public UniScopeMaptron xRemove(Object key) {
        this.remove(key);
        return this;
    }

    @Override
    public Set<K > keySet() {
        return this.mThisMap.keySet();
    }

    @Override
    public Collection<V > values() {
        return this.mThisMap.values();
    }

    @Override
    public Set<Entry<K, V > > entrySet() {
        return this.mThisMap.entrySet();
    }

    @Override
    public Iterator<Map.Entry<K, V> > iterator() {
        return this.mThisMap.entrySet().iterator();
    }

    @Override
    public V put( K key, V value ) {
        return this.mThisMap.put( key, value );
    }

    @Override
    public V putIfAbsent( K key, V value ) {
        return this.mThisMap.putIfAbsent( key, value );
    }

    @Override
    public boolean hasOwnProperty ( Object key ) {
        return this.mThisMap.containsKey( key );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.mThisMap );
    }

    @Override
    public Iterator<Map.Entry<K, V> > scopeIterator() {
        return new ScopeEntryIterator();
    }

    @Override
    public Set<Map.Entry<K,V> > scopeEntrySet() {
        Set<Map.Entry<K,V>> es;
        return (es = this.entrySet) == null ? (this.entrySet = new ScopeEntrySet()) : es;
    }

    @Override
    public Set<K > scopeKeySet() {
        Set<K> ks = this.scKeySet;
        if ( ks == null ) {
            ks = new ScopeKeySet();
            this.scKeySet = ks;
        }
        return ks;
    }

    @Override
    public Collection<V > scopeValues() {
        Collection<V> vs = this.scValues;
        if ( vs == null ) {
            vs = new ScopeValues();
            this.scValues = vs;
        }
        return vs;
    }

    protected final class ScopeEntrySet extends AbstractSet<Map.Entry<K,V> > {
        public final int size()                 { throw new UnsupportedOperationException("Iterator only."); }

        public final void clear()               { UniScopeMaptron.this.clear(); }

        public final Iterator<Map.Entry<K,V> > iterator() {
            return new ScopeEntryIterator();
        }

        public final boolean contains( Object o ) {
            if ( !(o instanceof Map.Entry) ) {
                return false;
            }
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            Object key = e.getKey();

            V v = UniScopeMaptron.this.get(key);
            return v != null && v.equals(e.getValue());
        }

        public final boolean remove( Object o ) {
            if ( this.contains(o) ) {
                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
                Object key = e.getKey();

                return UniScopeMaptron.this.remove(key) != null;
            }
            return false;
        }

        public final Spliterator<Map.Entry<K,V>> spliterator() {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT );
        }
    }

    protected abstract class ScopeIterator {
        protected Iterator<Map.Entry<K, V>> thisMapIterator;
        protected UniScopeMap<K, V>         currentScope;

        ScopeIterator() {
            this.thisMapIterator = mThisMap.entrySet().iterator();
            this.currentScope = UniScopeMaptron.this;
        }

        public boolean hasNext() {
            while ( !this.thisMapIterator.hasNext() && this.currentScope.parent() != null ) {
                this.currentScope    = this.currentScope.parent();
                this.thisMapIterator = this.currentScope.thisScope().entrySet().iterator();
            }
            return this.thisMapIterator.hasNext();
        }

        protected Map.Entry<K, V> nextNode() {
            if ( !this.hasNext() ) {
                throw new NoSuchElementException();
            }
            return this.thisMapIterator.next();
        }

        public void remove() {
            this.thisMapIterator.remove();
        }
    }

    final class ScopeKeySet extends AbstractSet<K> {
        public final int size()                 { throw new UnsupportedOperationException("Iterator only."); }

        public final void clear()               { UniScopeMaptron.this.clear(); }

        public final Iterator<K> iterator() {
            return new ScopeKeyIterator();
        }

        public final boolean contains( Object o ) { return containsKey(o); }

        public final boolean remove( Object key ) {
            return UniScopeMaptron.this.remove(key) != null;
        }

        public final Spliterator<K> spliterator()  {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT );
        }
    }

    protected final class ScopeKeyIterator extends ScopeIterator implements Iterator<K> {
        public final K next() { return nextNode().getKey(); }
    }

    final class ScopeValues extends AbstractCollection<V> {
        public final int size()                 { throw new UnsupportedOperationException("Iterator only."); }

        public final void clear()               { UniScopeMaptron.this.clear(); }

        public final Iterator<V> iterator() {
            return new ScopeValueIterator();
        }

        public final boolean contains( Object o ) { return containsValue(o); }

        public final Spliterator<V> spliterator() {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED );
        }
    }

    protected final class ScopeValueIterator extends ScopeIterator implements Iterator<V> {
        public final V next() { return (V)nextNode().getValue(); }
    }

    protected final class ScopeEntryIterator extends ScopeIterator implements Iterator<Map.Entry<K,V>> {
        public final Map.Entry<K,V> next() { return nextNode(); }
    }
}
