package com.pinecone.framework.unit.trie;

import com.pinecone.framework.system.prototype.Pinenut;

public class TrieReparseNode<K, V> implements Pinenut {
    protected AbstractTrieMap<K, V> trieMap ;
    protected TrieNode              target  ;
    protected String                path    ;

    public TrieReparseNode( String path, AbstractTrieMap<K, V> trieMap ) {
        this.trieMap = trieMap;
        TrieNode node = this.trieMap.getNode(path);
//        if ( node != null && node.isEnd ) {
//            this.target = node;
//        }
//        else {
//            throw new RuntimeException("Target node does not exist or is not a leaf node.");
//        }
        this.target = node;
        this.path = path;
    }

    public TrieMap<K, V> getTrieMap() {
        return trieMap;
    }

    public void setTrieMap( AbstractTrieMap<K, V> trieMap ) {
        this.trieMap = trieMap;
    }

    public TrieNode getTarget() {
        return this.target;
    }

    public void setTarget( TrieNode target ) {
        this.target = target;
    }

    public String getPath() {
        return path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String toString() {
        return "TrieReferenceNode{trieMap=" + trieMap + ", target=" + target + ", path='" + path + "'}";
    }
}
