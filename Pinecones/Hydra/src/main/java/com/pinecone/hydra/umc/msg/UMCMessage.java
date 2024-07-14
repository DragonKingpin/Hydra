package com.pinecone.hydra.umc.msg;

import java.io.InputStream;
import java.util.Map;

public interface UMCMessage extends Message {
    UMCHead     getHead();

    default UMCMethod   getMethod(){
        return this.getHead().getMethod();
    }

    default Map<String,Object > getExHead() {
        return this.getHead().joExtraHead;
    }

    byte[]      getBytesBody();

    InputStream getStreamBody() ;

    Object     getBody() ;
}
