package com.pinecone.summer.multiparts;


public class MultipartException extends RuntimeException {
    public MultipartException(String msg) {
        super(msg);
    }

    public MultipartException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
