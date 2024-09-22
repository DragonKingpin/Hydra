package com.pinecone.framework.unit.trie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.*;
import java.util.function.Supplier;

import com.pinecone.framework.util.Debug;
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
    protected final transient TrieNode root;
    protected final transient Supplier<Map<String, TrieNode>> mapSupplier;
    protected transient int size;
    protected transient TrieSegmentor segmentor;

    protected transient Set<Entry<K,V>> entrySet;
    protected transient Set<K> keySet;
    protected transient Collection<V> values;

    @SuppressWarnings( "unchecked" )
    public UniTrieMaptron( Supplier mapSupplier, TrieSegmentor segmentor ) {
        if ( mapSupplier == null ) {
            throw new IllegalArgumentException( "Map supplier cannot be null." );
        }
        this.mapSupplier  = mapSupplier;
        this.root         = new TrieNode( this.mapSupplier.get() , TrieNode.NodeType.Dir,"",null);
        this.size         = 0;
        this.segmentor    = segmentor;
    }

    public UniTrieMaptron( Supplier mapSupplier ) {
        this( mapSupplier, TrieSegmentor.DefaultSegmentor );
    }

    public UniTrieMaptron( TrieSegmentor segmentor) {
        this( HashMap::new, segmentor);
    }

    public UniTrieMaptron() {
        this( (Supplier) HashMap::new );
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

    public V reference ( K key, K target ) {
        TrieReparseNode p = new TrieReparseNode<>( target,this );
        this.putReference( key, p );
        return this.get( target );
    }

    public V putReference( K key, Object value ) {
        if ( key == null ) {
            throw new IllegalArgumentException( "Key cannot be null." );
        }
        String[] segments = this.segmentor.segments( key );
        TrieNode node = this.root;
        TrieNode parent = this.root;
        StringBuilder currentPath = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (i == 0){
                currentPath.append(segment);
            }
            else {
                currentPath.append( this.segmentor.getSeparator() ).append( segment );
            }

            if ( i < segments.length - 1 ) {
                node.children.putIfAbsent(segment, new TrieNode( this.mapSupplier.get(), TrieNode.NodeType.Dir,currentPath.toString(),parent ) );
                node = node.children.get(segment);
            }
            else {
                if ( node.children.containsKey(segment) ) {
                    node = node.children.get(segment);
                }
                else {
                    TrieNode.NodeType nodeType;
                    if (value instanceof TrieReparseNode) {
                        nodeType = TrieNode.NodeType.Reparse;
                    } else {
                        nodeType = TrieNode.NodeType.Value;
                    }
                    node.children.put(segment, new TrieNode(this.mapSupplier.get(), nodeType, currentPath.toString(),parent));
                    node = node.children.get(segment);
                }
            }
            parent = node;
        }

        if ( !node.isEnd ) {
            this.size++;
        }
        if ( node.nodeType == TrieNode.NodeType.Dir ){
            throw new RuntimeException("dir can't insert value");
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
        if (node.nodeType == TrieNode.NodeType.Dir){
            throw new RuntimeException("dir can't get value");
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

        return this.remove( this.root, this.segmentor.segments( key.toString() ), 0 );
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        for ( Entry<? extends K, ? extends V> entry : m.entrySet() ) {
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
        String[] segments = this.segmentor.segments( key );
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
    public Set<Entry<K,V > > entrySet() {
        Set<Entry<K,V > >  es = this.entrySet;
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
            Entry<?, ?> entry = ( Entry<?, ?> ) o;
            Object value = UniTrieMaptron.this.get( entry.getKey() );
            return Objects.equals( value, entry.getValue() );
        }

        @Override
        public boolean remove( Object o ) {
            if ( !( o instanceof Map.Entry ) ) {
                return false;
            }
            Entry<?, ?> entry = ( Entry<?, ?> ) o;
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
        private final Deque<Iterator<Entry<String, TrieNode>>> stack;
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
                Iterator<Entry<String, TrieNode>> iterator = this.stack.peek();
                if ( !iterator.hasNext() ) {
                    this.stack.pop();
                    if ( !this.pathStack.isEmpty() ) {
                        this.currentPath = this.pathStack.pop();
                    }
                    continue;
                }

                Entry<String, TrieNode> entry = iterator.next();
                TrieNode node = entry.getValue();
                String segment = entry.getKey();

                this.pathStack.push( new StringBuilder( this.currentPath ) );
                if ( this.currentPath.length() > 0 ) {
                    this.currentPath.append( UniTrieMaptron.this.segmentor.getSeparator() );
                }
                this.currentPath.append( segment );

//                while ( node.value instanceof TrieReparseNode ) {
//                    TrieReparseNode<K, V> reparseNode = ( TrieReparseNode<K, V> ) node.value;
//                    node = UniTrieMaptron.this.getNode( reparseNode.getPath() );
//                    if ( node == null ) {
//                        break;
//                    }
//                }

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

    public List<V > listValue( String key ){
        TrieNode node = this.getNode( key );
        if ( node.nodeType != TrieNode.NodeType.Dir ){
            throw new IllegalOperationException( "Values of target path that be listed should be `dir`." );
        }

        List<V > list = new ArrayList<>( node.children.size() );
        for ( Entry<String, TrieNode > kv : node.children.entrySet() ) {
            if( kv.getValue().value != null ) {
                list.add( this.convertValue( kv.getValue().value ) );
            }
        }
        return list;
    }

    public List<String > listItem( String key, ItemListMode mode ){
        TrieNode node = this.getNode( key );
        if ( node.nodeType != TrieNode.NodeType.Dir ){
            throw new IllegalOperationException( "Item of target path that be listed should be `dir`." );
        }

        List<String > list = new ArrayList<>( node.children.size() );
        for ( Entry<String, TrieNode > kv : node.children.entrySet() ) {
            TrieNode nd = kv.getValue();
            if( kv.getValue().nodeType == TrieNode.NodeType.Reparse ) {
                nd = this.evalReparsedTarget( (TrieReparseNode) kv.getValue().value );
                Debug.trace(node.path);
            }

            if( mode == ItemListMode.All || mode == ItemListMode.Dir ) {
                if( nd.nodeType == TrieNode.NodeType.Dir ) {
                    list.add( kv.getKey() );
                }
            }

            if( mode == ItemListMode.All || mode == ItemListMode.Value ) {
                if( nd.nodeType == TrieNode.NodeType.Value ) {
                    list.add( kv.getKey() );
                }
            }
        }
        return list;
    }

    private TrieNode evalReparsedTarget(TrieReparseNode trieReparseNode){
        TrieNode node = this.getNode(trieReparseNode.getPath());
        while (node.nodeType == TrieNode.NodeType.Reparse){
            TrieReparseNode temporaryTrieReparseNode = ( TrieReparseNode ) node.value;
            node = this.getNode( trieReparseNode.getPath() );
        }
        return node;
    }

    public TrieMap clone(){
        UniTrieMaptron<String, String> cloneTree = new UniTrieMaptron<>();
       for (String key : this.keySet){
           TrieNode node = this.getNode(key);
           if (node.nodeType != TrieNode.NodeType.Reparse){
               cloneTree.put(key,node.value.toString());
           }
           else {
               TrieReparseNode trieReparseNode = ( TrieReparseNode ) node.value;
               TrieReparseNode temporaryTrieReparseNode = new TrieReparseNode(trieReparseNode.path,cloneTree);
               cloneTree.putReference(key,temporaryTrieReparseNode);
           }
       }
       return cloneTree;
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
    public String getSeparator(){
        return this.segmentor.getSeparator();
    }
}
