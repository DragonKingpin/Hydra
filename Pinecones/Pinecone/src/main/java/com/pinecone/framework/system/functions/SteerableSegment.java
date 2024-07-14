package com.pinecone.framework.system.functions;

import java.util.Map;

public interface SteerableSegment {
    Map<String, Object > data();

    String name();

    Object invoke( String fnName, Object...args ) throws Exception;

    void dispatch( Object...args ) throws Exception;
}