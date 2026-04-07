package com.pinecone.message;

import org.springframework.http.HttpStatus;

import com.pinecone.framework.system.prototype.Pinenut;

public abstract class ArchResponse implements Pinenut {

    private Boolean    success;
    private Integer    code = HttpStatus.OK.value();
    private String     message;
    private String     requestId;
    private String     errorCode;

}
