package com.pinecone.hydra.umb.broadcast.converter;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ResultBytesConverter<V> extends Pinenut {
    byte[] convert( V value );
}
