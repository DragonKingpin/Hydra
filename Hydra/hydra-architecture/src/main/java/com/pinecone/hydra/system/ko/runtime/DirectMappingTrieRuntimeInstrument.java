package com.pinecone.hydra.system.ko.runtime;

import java.util.Collection;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.unit.trie.DirectoryNode;
import com.pinecone.framework.unit.trie.TrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface DirectMappingTrieRuntimeInstrument extends RuntimeInstrument, PineUnit {

    TrieNode<TreeNode> getOwnProperty( String path );

    DirectoryNode<TreeNode > fetchOwnChildren( String path );

    Collection<String > fetchOwnMappingPath();

}
