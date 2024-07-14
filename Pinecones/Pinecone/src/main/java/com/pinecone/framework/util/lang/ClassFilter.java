package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ClassFilter extends Pinenut {
    boolean match( Class<? > clazz, ClassScopeLoader loader ) ;
}
