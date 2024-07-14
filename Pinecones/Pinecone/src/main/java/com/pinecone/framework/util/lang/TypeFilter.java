package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;

public interface TypeFilter extends Pinenut {
    boolean match( String szClassName, Object pool ) throws IOException;
}
