package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.name.Name;

public interface ClassScopeLoader extends Pinenut {
    Class<? > load ( Name simpleName ) throws ClassNotFoundException ;

    void clearCache();
}
