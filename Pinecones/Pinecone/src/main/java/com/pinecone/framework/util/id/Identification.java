package com.pinecone.framework.util.id;

import java.io.Serializable;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Identification extends Pinenut, Serializable {
    String toString();

    Identification parse( String hexID );
}
