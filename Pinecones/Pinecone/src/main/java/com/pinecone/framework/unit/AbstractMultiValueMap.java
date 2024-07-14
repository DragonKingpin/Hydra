package com.pinecone.framework.unit;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.AbstractCollection;

public abstract class AbstractMultiValueMap<K, V > implements MultiValueMapper<K, V > {
    private transient EntryCollection     mEntryCollection;
    private transient ValueCollection     mValueCollection;

    @Override
    public Collection<Map.Entry<K, V > > collection() {
        Collection<Map.Entry<K,V > >  es = this.mEntryCollection;
        return (es != null) ? es : ( this.mEntryCollection = new EntryCollection( this ) );
    }

    @Override
    public Collection<V > collectionValues(){
        Collection<V> vs = this.mValueCollection;
        return (vs != null) ? vs : ( this.mValueCollection = new ValueCollection( this ) );
    }

    class DummyEntry extends KeyValue<K, V > {
        public DummyEntry( K key, V value ) {
            super( key, value );
        }

        public void setKey( K key ) {
            this.key = key;
        }
    }

    class EntryIterator implements Iterator<Map.Entry<K, V > > {
        private final Iterator<? extends Map.Entry<K, ? extends Collection<V > > > entryIterator;
        private Iterator<V > currentCollectionIterator;
        private K currentKey;
        protected DummyEntry dummyEntry = new DummyEntry( null, null );

        EntryIterator( MultiValueMapper<K, V > that ) {
            this.entryIterator = that.entrySet().iterator();
            this.currentCollectionIterator = Collections.emptyIterator();
        }

        @Override
        public boolean hasNext() {
            while ( !this.currentCollectionIterator.hasNext() && this.entryIterator.hasNext() ) {
                Map.Entry<K, ? extends Collection<V > > entry = this.entryIterator.next();
                this.currentKey = entry.getKey();
                this.currentCollectionIterator = entry.getValue().iterator();
            }
            return this.currentCollectionIterator.hasNext();
        }

        @Override
        public Map.Entry<K, V > next() {
            if ( !this.hasNext() ) {
                throw new NoSuchElementException();
            }

            this.dummyEntry.setKey( this.currentKey );
            this.dummyEntry.setValue( this.currentCollectionIterator.next() );
            return this.dummyEntry;
        }
    }

    class EntryCollection extends AbstractCollection<Map.Entry<K, V > > {
        MultiValueMapper<K, V > map;

        EntryCollection( MultiValueMapper<K, V > that ) {
            this.map = that;
        }

        @Override
        public Iterator<Map.Entry<K, V > > iterator() {
            return new EntryIterator( this.map );
        }

        @Override
        public int size() {
            int size = 0;
            for ( Collection<V> values : this.map.values() ) {
                size += values.size();
            }
            return size;
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public void clear() {
            this.map.clear();
        }

        @Override
        public boolean contains( Object o ) {
            if ( !(o instanceof Map.Entry) ) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Collection<V> values = this.map.get(entry.getKey());
            return values != null && values.contains( entry.getValue() );
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Collection<V> values = this.map.get(entry.getKey());
            if ( values != null && values.remove( entry.getValue() ) ) {
                if ( values.isEmpty() ) {
                    this.map.remove( entry.getKey() );
                }
                return true;
            }
            return false;
        }
    }

    class ValueIterator implements Iterator<V > {
        private final Iterator<? extends Map.Entry<K, ? extends Collection<V>>> entryIterator;
        private Iterator<V> currentCollectionIterator;

        ValueIterator(MultiValueMapper<K, V> that) {
            this.entryIterator = that.entrySet().iterator();
            this.currentCollectionIterator = Collections.emptyIterator();
        }

        @Override
        public boolean hasNext() {
            while ( !this.currentCollectionIterator.hasNext() && this.entryIterator.hasNext() ) {
                Map.Entry<K, ? extends Collection<V > > entry = this.entryIterator.next();
                this.currentCollectionIterator = entry.getValue().iterator();
            }
            return this.currentCollectionIterator.hasNext();
        }

        @Override
        public V next() {
            if ( !this.hasNext() ) {
                throw new NoSuchElementException();
            }
            return this.currentCollectionIterator.next();
        }
    }

    class ValueCollection extends AbstractCollection<V> {
        MultiValueMapper<K, V> map;

        ValueCollection( MultiValueMapper<K, V> that ) {
            this.map = that;
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator(this.map);
        }

        @Override
        public int size() {
            int size = 0;
            for ( Collection<V> values : this.map.values() ) {
                size += values.size();
            }
            return size;
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public void clear() {
            this.map.clear();
        }

        @Override
        public boolean contains( Object o ) {
            for ( Collection<V> values : this.map.values() ) {
                if ( values.contains(o) ) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean remove( Object o ) {
            for ( Map.Entry<K, ? extends Collection<V > > entry : this.map.entrySet() ) {
                Collection<V > values = entry.getValue();
                if ( values.remove(o) ) {
                    if ( values.isEmpty() ) {
                        this.map.remove( entry.getKey() );
                    }
                    return true;
                }
            }
            return false;
        }
    }
}
