package com.pinecone.framework.unit.trie;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TrieSegmentor extends Pinenut {
    TrieSegmentor DefaultSegmentor = new SeparatedSegmentor();

    String[] segments( String szPathKey );

    String getSeparator();
}
