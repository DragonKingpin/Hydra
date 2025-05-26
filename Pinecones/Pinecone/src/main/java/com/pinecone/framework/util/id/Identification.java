package com.pinecone.framework.util.id;

import java.io.Serializable;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Identification extends Pinenut, Serializable, Comparable<Identification> {
    Identification parse( String hexID );
}
