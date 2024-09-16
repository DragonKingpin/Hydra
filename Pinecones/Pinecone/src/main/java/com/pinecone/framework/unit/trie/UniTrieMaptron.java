package com.pinecone.framework.unit.trie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.*;
import java.util.function.Supplier;

import com.pinecone.framework.util.json.JSON;

/**
 *  Pinecone Ursus For Java UniTrieMaptron
 *  SharedList Author: Ken, DragonKing
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  **********************************************************
 *  Thanks for Ken`s contribution.
 *  **********************************************************
 */
public class UniTrieMaptron<K extends String, V > extends AbstractTrieMap<K, V > implements TrieMap<K, V > {
    private final transient TrieNode root;
    private final transient Supplier<Map<String, TrieNode>> mapSupplier;
    private transient int size;
    private transient String separator;

    protected transient Set<Map.Entry<K,V> > entrySet;
    protected transient Set<K> keySet;
    protected transient Collection<V> values;

    @SuppressWarnings( "unchecked" )
    public UniTrieMaptron( Supplier mapSupplier, String szSeparator ) {
        if ( mapSupplier == null ) {
            throw new IllegalArgumentException( "Map supplier cannot be null." );
        }
        this.mapSupplier  = mapSupplier;
        this.root         = new TrieNode( this.mapSupplier.get() );
        this.size         = 0;
        this.separator    = szSeparator;
    }

    public UniTrieMaptron( Supplier mapSupplier ) {
        this( mapSupplier, "/" );
    }

    public UniTrieMaptron( String szSeparator ) {
        this( HashMap::new, szSeparator );
    }

    public UniTrieMaptron() {
        this( HashMap::new );
    }




    @SuppressWarnings( "unchecked" )
    protected V convertValue( Object value ) {
        return ( V ) value;
    }

    @Override
    public boolean hasOwnProperty( Object elm ) {
        return this.containsKey( elm );
    }

    @Override
    public V put( K key, V value ) {
        return this.putReference( key, value );
    }

    public V putReference( K key, Object value ) {
        if ( key == null ) {
            throw new IllegalArgumentException( "Key cannot be null." );
        }
        String[] segments = key.split( this.separator );
        TrieNode node = this.root;

        for ( String segment : segments ) {
            node.children.putIfAbsent( segment, new TrieNode( this.mapSupplier.get() ) );
            node = node.children.get( segment );
        }

        if ( !node.isEnd ) {
            this.size++;
        }
        node.isEnd = true;
        Object oldValue = node.value;
        node.value = value;

        return this.convertValue( oldValue );
    }

    @Override
    public V get( Object key ) {
        if ( !(key instanceof String) ) {
            key = key.toString();
        }

        TrieNode node = this.getNode( ( String ) key );
        if ( node == null ) {
            return null;
        }
        while ( node.value instanceof TrieReparseNode ) {
            TrieReparseNode trieReparseNode = ( TrieReparseNode ) node.value;
            node = this.getNode( trieReparseNode.getPath() );
        }
        return node.isEnd ? this.convertValue( node.value ) : null;
    }

    @Override
    public boolean containsKey( Object key ) {
        if ( !( key instanceof String ) ) {
            key = key.toString();
        }

        TrieNode node = this.getNode( ( String ) key );
        return node != null && node.isEnd;
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.containsValueRecursive( this.root, value );
    }

    private boolean containsValueRecursive( TrieNode node, Object value ) {
        if ( node == null ) {
            return false;
        }
        if ( node.isEnd && Objects.equals( node.value, value ) ) {
            return true;
        }
        for ( TrieNode child : node.children.values() ) {
            if ( this.containsValueRecursive( child, value ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V remove( Object key ) {
        if ( !(key instanceof String) ) {
            key = key.toString();
        }

        return this.remove( this.root, ( ( String ) key ).split( this.separator ), 0 );
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        for ( Map.Entry<? extends K, ? extends V> entry : m.entrySet() ) {
            this.put( entry.getKey(), entry.getValue() );
        }
    }

    @Override
    public void clear() {
        this.root.children.clear();
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public TrieNode getNode( String key ) {
        String[] segments = key.split( this.separator );
        TrieNode node = this.root;

        for ( String segment : segments ) {
            node = node.children.get( segment );
            if ( node == null ) {
                return null;
            }
        }

        return node;
    }

    protected V remove( TrieNode node, String[] segments, int depth ) {
        if ( node == null ) {
            return null;
        }

        if ( depth == segments.length ) {
            if ( !node.isEnd ) {
                return null;
            }
            node.isEnd = false;
            V oldValue = this.convertValue( node.value );
            node.value = null;
            --this.size;
            return oldValue;
        }

        String segment = segments[depth];
        TrieNode nextNode = node.children.get( segment );
        V result = this.remove( nextNode, segments, depth + 1 );

        if ( nextNode != null && nextNode.children.isEmpty() && !nextNode.isEnd ) {
            node.children.remove( segment );
        }

        return result;
    }

    @Override
    public Set<K> keySet() {
        Set<K >  es = this.keySet;
        return (es != null) ? es : (this.keySet = new KeySet());
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
    }

    @Override
    public Set<Map.Entry<K,V > > entrySet() {
        Set<Map.Entry<K,V > >  es = this.entrySet;
        return (es != null) ? es : (this.entrySet = new EntrySet());
    }



    class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return UniTrieMaptron.this.size();
        }

        @Override
        public boolean contains( Object o ) {
            if ( !( o instanceof Map.Entry ) ) {
                return false;
            }
            Map.Entry<?, ?> entry = ( Map.Entry<?, ?> ) o;
            Object value = UniTrieMaptron.this.get( entry.getKey() );
            return Objects.equals( value, entry.getValue() );
        }

        @Override
        public boolean remove( Object o ) {
            if ( !( o instanceof Map.Entry ) ) {
                return false;
            }
            Map.Entry<?, ?> entry = ( Map.Entry<?, ?> ) o;
            K key = ( K ) entry.getKey();
            V currentValue = UniTrieMaptron.this.get( key );
            if ( Objects.equals( currentValue, entry.getValue() ) ) {
                UniTrieMaptron.this.remove( key );
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            UniTrieMaptron.this.clear();
        }
    }

    class EntryIterator implements Iterator<Entry<K, V>> {
        private final Deque<Iterator<Map.Entry<String, TrieNode>>> stack;
        private final Deque<StringBuilder> pathStack;
        private Entry<K, V> nextEntry;
        private StringBuilder currentPath;

        public EntryIterator() {
            this.stack = new ArrayDeque<>();
            this.pathStack = new ArrayDeque<>();
            this.stack.push( UniTrieMaptron.this.root.children.entrySet().iterator() );
            this.currentPath = new StringBuilder();

            this.advance();
        }

        private void advance() {
            this.nextEntry = null;

            while ( !this.stack.isEmpty() ) {
                Iterator<Map.Entry<String, TrieNode>> iterator = this.stack.peek();
                if ( !iterator.hasNext() ) {
                    this.stack.pop();
                    if ( !this.pathStack.isEmpty() ) {
                        this.currentPath = this.pathStack.pop();
                    }
                    continue;
                }

                Map.Entry<String, TrieNode> entry = iterator.next();
                TrieNode node = entry.getValue();
                String segment = entry.getKey();

                this.pathStack.push( new StringBuilder( this.currentPath ) );
                if ( this.currentPath.length() > 0 ) {
                    this.currentPath.append( UniTrieMaptron.this.separator );
                }
                this.currentPath.append( segment );

                while ( node.value instanceof TrieReparseNode ) {
                    TrieReparseNode<K, V> reparseNode = ( TrieReparseNode<K, V> ) node.value;
                    node = UniTrieMaptron.this.getNode( reparseNode.getPath() );
                    if ( node == null ) {
                        break;
                    }
                }

                if ( node == null ) {
                    continue;
                }

                if ( node.isEnd ) {
                    this.nextEntry = new AbstractMap.SimpleEntry<>( ( K ) this.currentPath.toString(), UniTrieMaptron.this.convertValue( node.value ) );
                    this.stack.push( node.children.entrySet().iterator() );
                    break;
                }

                this.stack.push( node.children.entrySet().iterator() );
            }
        }

        @Override
        public boolean hasNext() {
            return this.nextEntry != null;
        }

        @Override
        public Entry<K, V> next() {
            if ( !this.hasNext() ) {
                throw new NoSuchElementException();
            }

            Entry<K, V> entry = this.nextEntry;
            this.advance();
            return entry;
        }
    }

    class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return UniTrieMaptron.this.size();
        }

        @Override
        public boolean contains( Object o ) {
            return UniTrieMaptron.this.containsKey( o );
        }

        @Override
        public boolean remove( Object o ) {
            return UniTrieMaptron.this.remove( o ) != null;
        }

        @Override
        public void clear() {
            UniTrieMaptron.this.clear();
        }
    }

    class KeyIterator implements Iterator<K> {
        private final Iterator<Entry<K, V>> entryIterator;

        public KeyIterator() {
            this.entryIterator = UniTrieMaptron.this.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return this.entryIterator.hasNext();
        }

        @Override
        public K next() {
            return this.entryIterator.next().getKey();
        }
    }

    class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                private final Iterator<Entry<K, V>> entryIterator = UniTrieMaptron.this.entrySet().iterator();

                @Override
                public boolean hasNext() {
                    return this.entryIterator.hasNext();
                }

                @Override
                public V next() {
                    return this.entryIterator.next().getValue();
                }
            };
        }

        @Override
        public int size() {
            return UniTrieMaptron.this.size();
        }

        @Override
        public boolean contains( Object o ) {
            return UniTrieMaptron.this.containsValue(o);
        }
    }


    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
