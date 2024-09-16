package com.pinecone.framework.unit.trie;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class TrieNode implements Pinenut {
    Map<String, TrieNode > children;
    Object  value       = null;
    boolean isEnd = false;

    TrieNode( Map<String, TrieNode > map ) {
        this.children = map;
    }
}
