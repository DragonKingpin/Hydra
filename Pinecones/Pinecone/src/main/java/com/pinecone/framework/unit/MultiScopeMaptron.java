package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.json.JSON;

import java.io.Serializable;
import java.util.*;

public class MultiScopeMaptron<K, V > implements PineUnit, Map<K, V >, MultiScopeMap<K, V >, Cloneable, Serializable, Iterable<Map.Entry<K, V > > {
    protected String                            mszName    ;

    protected List<MultiScopeMap<K, V > >       mParents ;

    protected Map<K, V>                         mThisMap ;

    protected transient Set<Map.Entry<K,V> >    entrySet ;
    protected transient Set<K>                  scKeySet ;
    protected transient Collection<V>           scValues ;


    public MultiScopeMaptron() {
        this( true, null );
    }

    public MultiScopeMaptron( String name ) {
        this( true, null );
        this.setName( name );
    }

    public MultiScopeMaptron( Map<K, V > thisMap, List<MultiScopeMap<K, V > > prototypes, String name ){
        this.mThisMap  = thisMap;
        this.mParents  = prototypes;
        this.mszName   = name;

        if( this.mThisMap == null ) {
            this.mThisMap = new LinkedHashMap<>();
        }
    }

    public MultiScopeMaptron( Map<K, V > thisMap, List<MultiScopeMap<K, V > > prototypes ){
        this( thisMap, prototypes, "" );
    }

    public MultiScopeMaptron( boolean bLinked, List<MultiScopeMap<K, V > > prototypes ){
        this( bLinked ? new LinkedHashMap<>() : new HashMap<>(), prototypes );
    }

    public MultiScopeMaptron( Map<K, V > thisMap ){
        this( thisMap, null );
    }





    @Override
    public String                         getName       () {
        return this.mszName;
    }

    @Override
    public MultiScopeMap<K, V >           setName       ( String name ) {
        this.mszName = name;
        return this;
    }

    @Override
    public List<MultiScopeMap<K, V > >    getParents    () {
        return this.mParents;
    }

    @Override
    public Map<K, V >                     thisScope  () {
        return this.mThisMap;
    }

    @Override
    public MultiScopeMap<K, V >           setParents    ( List<MultiScopeMap<K, V > > that ) {
        this.mParents = that;
        return this;
    }

    @Override
    public MultiScopeMap<K, V >           setThisScope  ( Map<K, V > that ) {
        this.mThisMap = that;
        return this;
    }

    @Override
    public MultiScopeMap<K, V >           addParent     ( MultiScopeMap<K, V > that ) {
        if( this.getParents() == null ) {
            this.mParents = new ArrayList<>();
        }
        this.mParents.add( that );
        return this;
    }

    @Override
    public ScopeMap<K, V >                elevate       ( Map<K, V > child ) {
        MultiScopeMaptron<K, V > sup = new MultiScopeMaptron<>( this.mThisMap, this.mParents );
        this.setThisScope ( child );
        ArrayList<MultiScopeMap<K, V > > a = new ArrayList<>();
        a.add( sup );
        this.setParents   ( a     );
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
        if ( !result && this.mParents != null ) {
            for ( MultiScopeMap<K, V > m : this.mParents ) {
                result = m.containsKey( key );
                if( result ) {
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public boolean containsValue( Object value ) {
        boolean result = this.mThisMap.containsValue(value);
        if ( !result && this.mParents != null ) {
            for ( MultiScopeMap<K, V > m : this.mParents ) {
                result = m.containsValue( value );
                if( result ) {
                    break;
                }
            }
        }

        return result;
    }


    // For Multiple-Inheritance Scope, for ambiguous sibling-super-key, it will only find in the nearest parent.
    @Override
    public V get( Object key ) {
        V val = this.mThisMap.get( key );
        if ( val == null && this.mParents != null ) {
            for ( MultiScopeMap<K, V > m : this.mParents ) {
                val = m.get( key );
                if( val != null ) {
                    break;
                }
            }
        }

        return val;
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        this.mThisMap.putAll( m );
    }

    public MultiScopeMaptron xPutAll(Map<? extends K, ? extends V> m ) {
        this.putAll(m);
        return this;
    }

    @Override
    public void clear() {
        this.mThisMap.clear();
    }

    public MultiScopeMaptron xClear() {
        this.clear();
        return this;
    }

    @Override
    public V remove( Object key ) {
        V v = this.mThisMap.remove(key);
        if ( v == null && this.mParents != null ) {
            for ( MultiScopeMap<K, V > m : this.mParents ) {
                v = m.remove( key );
                if( v != null ) {
                    break;
                }
            }
        }

        return v;
    }

    public MultiScopeMaptron xRemove( Object key ) {
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
    public Set<Map.Entry<K, V >> entrySet() {
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
        return null;
    }

    @Override
    public Set<Map.Entry<K,V> > scopeEntrySet() {
        Set<Map.Entry<K,V>> es;
        return (es = this.entrySet) == null ? (this.entrySet = new ScopeEntrySet()) : es;
    }

    @Override
    public Set<K > scopeKeySet() {
        return null;
    }

    @Override
    public Collection<V > scopeValues() {
        return null;
    }







    protected final class ScopeEntrySet extends AbstractSet<Map.Entry<K,V> > {
        public final int size()                 { throw new UnsupportedOperationException("Iterator only."); }

        public final void clear()               { MultiScopeMaptron.this.clear(); }

        public final Iterator<Map.Entry<K,V> > iterator() {
            return new ScopeEntryIterator();
        }

        public final boolean contains( Object o ) {
            if ( !(o instanceof Map.Entry) ) {
                return false;
            }
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            Object key = e.getKey();

            V v = MultiScopeMaptron.this.get(key);
            return v != null && v.equals(e.getValue());
        }

        public final boolean remove( Object o ) {
            if ( this.contains(o) ) {
                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
                Object key = e.getKey();

                return MultiScopeMaptron.this.remove(key) != null;
            }
            return false;
        }

        public final Spliterator<Map.Entry<K,V>> spliterator() {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT );
        }
    }

    protected abstract class ScopeIterator {
        protected MultiScopeMap<K, V >[]            parentsStack;
        protected int                               stackAt;
        protected Iterator<Map.Entry<K, V> >        currentIterator;

        public ScopeIterator() {
            this.parentsStack     = (MultiScopeMap<K, V >[]) MultiScopeMaptron.this.ancestors();
            this.stackAt          = 0;
            this.currentIterator  = MultiScopeMaptron.this.mThisMap.entrySet().iterator();
        }

        private boolean parentsHasNext() {
            boolean b = this.stackAt < this.parentsStack.length;
            if( b ) {
                MultiScopeMap<K, V> parentMap = this.parentsStack[ this.stackAt ];
                if( parentMap != null && parentMap.thisScope() != null ) {
                    return true;
                }
            }
            return b;
        }

        public boolean hasNext() {
            if ( this.currentIterator.hasNext() ) {
                return true;
            }
            else {
                while ( parentsHasNext() ) {
                    MultiScopeMap<K, V> parentMap = this.parentsStack[ this.stackAt ];
                    ++this.stackAt;
                    if ( parentMap != null ) {
                        this.currentIterator = parentMap.thisScope().entrySet().iterator();
                        if ( this.currentIterator.hasNext() ) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        protected Map.Entry<K, V> nextNode() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return this.currentIterator.next();
        }

        public void remove() {
            this.currentIterator.remove();
        }
    }

    final class ScopeKeySet extends AbstractSet<K> {
        public final int size()                 { throw new UnsupportedOperationException("Iterator only."); }

        public final void clear()               { MultiScopeMaptron.this.clear(); }

        public final Iterator<K> iterator() {
            return new ScopeKeyIterator();
        }

        public final boolean contains( Object o ) { return containsKey(o); }

        public final boolean remove( Object key ) {
            return MultiScopeMaptron.this.remove(key) != null;
        }

        public final Spliterator<K> spliterator()  {
            return Spliterators.spliterator( this, Spliterator.SIZED | Spliterator.ORDERED | Spliterator.DISTINCT );
        }
    }

    protected final class ScopeKeyIterator extends ScopeIterator implements Iterator<K> {
        public final K next() { return nextNode().getKey(); }
    }

    final class ScopeValues extends AbstractCollection<V> {
        public final int size()                 { throw new IllegalStateException("Iterator only."); }

        public final void clear()               { MultiScopeMaptron.this.clear(); }

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
