package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TypeConverter extends Pinenut {
    Object converter( String val, String type );

    String queryType( Object val );

    String queryRecognizedType( String type );

    boolean isJSON( Object val );
}
