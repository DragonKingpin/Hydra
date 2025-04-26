package com.pinecone.framework.util.id;

public interface NumbernicID extends Identification {
    long longVal();

    int intVal();

    @Override
    default String toJSONString() {
        return this.toString();
    }
}
