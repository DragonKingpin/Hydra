package com.pinecone.framework.unit.trie;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UniTrieTree implements Pinenut {
    protected UniTrieMaptron <String,Object> uniTrieMaptron = new UniTrieMaptron<>();

    public UniTrieTree(){};

    public void insert(String key,Object value){
        this.uniTrieMaptron.put(key,value);
    }

    public void remove(String key){
        this.uniTrieMaptron.remove(key);
    }

    public Object get(String key){
        return this.uniTrieMaptron.get(key);
    }

    public Set<Map.Entry<String,Object>> getAllEntry(){
        return this.uniTrieMaptron.entrySet;
    }

    public Set<String> getAllKey(){
        return this.uniTrieMaptron.keySet;
    }

    public Collection<Object> getAllValue(){
        return this.uniTrieMaptron.values;
    }

    public List<String> prefixSearch(String key){
        return this.uniTrieMaptron.listItem(key, AbstractTrieMap.ItemListMode.All);
    }
}
