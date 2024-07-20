package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.lang.iterator.NamespaceIterator;

public class ClassIteratorPair implements Pinenut {
    public NamespaceIterator classIter;
    public NamespaceIterator packageIter;

    ClassIteratorPair( NamespaceIterator classIter, NamespaceIterator packageIter ) {
        this.classIter    = classIter;
        this.packageIter  = packageIter;
    }
}
