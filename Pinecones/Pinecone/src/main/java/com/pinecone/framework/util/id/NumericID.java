package com.pinecone.framework.util.id;

public interface NumericID extends Identification {

    long longVal();

    int intVal();

    int sizeof();

    default int bitsof() {
        return this.sizeof() * 8;
    }

    @Override
    default String toJSONString() {
        return this.toString();
    }

    byte[] toBytesLE();

    byte[] toBytesBE();

    // Pinecone is using uniformed Little-Endian by default.
    @Override
    default byte[] toBytes() {
        return this.toBytesLE();
    }

}
