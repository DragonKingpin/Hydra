package com.pinecone.framework.util.json;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;


public interface JSONCompiler extends Pinenut {
    OutputStream compile( Map that, OutputStream outputStream ) throws IOException;

    OutputStream compile( Collection that, OutputStream outputStream ) throws IOException ;

    OutputStream compile( Object[] those, OutputStream outputStream ) throws IOException ;

    OutputStream compile( Object that, OutputStream outputStream ) throws IOException ;
}
