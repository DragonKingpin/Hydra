package com.pinecone.framework.util.lang.iterator;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Iterator;

public interface NamespaceIterator extends Iterator<String >, Pinenut {
    boolean hasNext();

    String next();
}
