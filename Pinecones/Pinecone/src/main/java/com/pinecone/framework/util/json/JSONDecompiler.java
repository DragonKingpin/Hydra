package com.pinecone.framework.util.json;

import com.pinecone.framework.system.prototype.Pinenut;

public interface JSONDecompiler extends Pinenut {
    Object nextValue( Object parent ) throws JSONCompilerException ;

    Object nextValue() throws JSONCompilerException ;

    Object decompile( Object parent ) ;

    Object decompile() ;
}
