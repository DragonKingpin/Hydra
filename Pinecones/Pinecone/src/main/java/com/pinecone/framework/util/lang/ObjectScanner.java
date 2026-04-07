package com.pinecone.framework.util.lang;

import java.io.IOException;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.lang.iterator.NamespaceIterator;

public interface ObjectScanner extends Pinenut {

    void addIncludeFilter     ( TypeFilter filter          );

    void addExcludeFilter     ( TypeFilter filter          );

    void addIterator          ( NamespaceIterator classIter, NamespaceIterator packageIter ) ;

    void scan( String szNSName, boolean bCollectChildPackage, List<String > candidates ) throws IOException;

}
