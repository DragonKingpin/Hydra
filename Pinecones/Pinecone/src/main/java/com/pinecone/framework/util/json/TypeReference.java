package com.pinecone.framework.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {
    protected final Type _type;

    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        else {
            this._type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
        }
    }

    public Type getType() {
        return this._type;
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
