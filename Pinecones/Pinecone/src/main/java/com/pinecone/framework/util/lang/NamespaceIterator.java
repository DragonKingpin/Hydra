package com.pinecone.framework.util.lang;

import java.util.Iterator;

public interface NamespaceIterator extends Iterator<String > {
    boolean hasNext();

    String next();
}
