package com.pinecone.framework.unit.trie;

import java.util.Map;

public class TrieNode{
    Object value = null;  // 存储当前节点的值
    Map<String, TrieNode> children; // 子节点，使用路径段作为键
    boolean isEnd = false;  // 是否为某个完整路径的结束节点

    // 构造函数允许传入自定义的Map类型
    TrieNode(Map<String, TrieNode> map) {
        this.children = map;
    }
}
