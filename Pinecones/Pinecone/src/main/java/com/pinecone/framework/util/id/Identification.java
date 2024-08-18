package com.pinecone.framework.util.id;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Identification extends Pinenut {
    String toString();

     Identification  parse(String ID);
}
