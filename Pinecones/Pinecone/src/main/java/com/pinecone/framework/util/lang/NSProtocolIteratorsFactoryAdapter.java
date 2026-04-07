package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.util.List;

public interface NSProtocolIteratorsFactoryAdapter extends Pinenut {
    void prepareScopeIterators ( String szNSName, List<NamespaceIteratorPair> pairs ) throws IOException;

    void prepareIterators      ( String szNSName, List<NamespaceIteratorPair> pairs ) throws IOException;
}
