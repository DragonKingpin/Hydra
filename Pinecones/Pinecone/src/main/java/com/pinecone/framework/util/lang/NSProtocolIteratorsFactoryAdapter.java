package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

public interface NSProtocolIteratorsFactoryAdapter extends Pinenut {
    void prepareScopeIterators ( String szNSName, List<ClassIteratorPair > pairs ) throws IOException;

    void prepareIterators      ( String szNSName, List<ClassIteratorPair > pairs ) throws IOException;
}
