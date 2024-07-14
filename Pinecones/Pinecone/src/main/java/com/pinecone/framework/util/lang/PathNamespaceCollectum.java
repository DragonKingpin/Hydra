package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;

public interface PathNamespaceCollectum extends Pinenut {
    boolean matched      ( String szProtocol );

    void    collect      ( String szResourcePath, String szPackageName, List<String > classNames, boolean bCollectChildren );

    String  collectFirst ( String szResourcePath, String szPackageName );
}
