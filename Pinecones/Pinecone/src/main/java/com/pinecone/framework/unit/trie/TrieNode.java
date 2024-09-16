package com.pinecone.framework.unit.trie;

import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class TrieNode implements Pinenut {
    TrieNode               parent;
    Map<String, TrieNode > children;
    Object  value       = null;
    boolean isEnd       = false;
    String  path;
    NodeType nodeType;
    enum NodeType {
        Dir,
        Value,
        Reparse
    }

    TrieNode( Map<String, TrieNode > map ,NodeType nodeType,String path) {
        this.children = map;
        this.nodeType = nodeType;
        this.path = path;
    }

    public String put( String key, Object value,TrieMap trieMap ){
        if ( this.nodeType == NodeType.Dir ){
            String childPath = this.path + trieMap.getSeparator() + key;
            trieMap.put( childPath,value );
            return childPath;
        }
        else {
            throw new RuntimeException("illegal node！！！");
        }
    }


}
