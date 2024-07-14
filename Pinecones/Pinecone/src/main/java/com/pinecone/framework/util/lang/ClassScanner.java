package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.util.List;

public interface ClassScanner extends Pinenut {
    void addIncludeFilter     ( TypeFilter filter          );

    void addExcludeFilter     ( TypeFilter filter          );

    void addIterator          ( NamespaceIterator classIter, NamespaceIterator packageIter ) ;

    void scan( String szNSName, boolean bCollectChildPackage, List<String > candidates ) throws IOException ;
}
