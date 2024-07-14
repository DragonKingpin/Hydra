package com.pinecone.slime.source;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Date;

public interface ResultConverter<V > extends Pinenut {
    V convert( Object val );

    static boolean isPrimitiveOrSpecialType(Class<?> type) {
        return type.isPrimitive() ||
                type == String.class ||
                Number.class.isAssignableFrom(type) ||
                type == Boolean.class ||
                type == Character.class ||
                type == Date.class ||
                type.isEnum() ||
                type == byte[].class;
    }
}
