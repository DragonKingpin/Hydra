package com.pinecone.framework.util.id;

public interface BytesID extends Identification {

    int length();

    String toHexString();

    String toBase64String();

}
