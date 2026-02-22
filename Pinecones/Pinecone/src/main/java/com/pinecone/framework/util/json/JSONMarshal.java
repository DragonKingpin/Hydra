package com.pinecone.framework.util.json;

import com.pinecone.framework.util.json.handler.EncodeHandlerRegistry;
import com.pinecone.framework.util.json.handler.JSONObjectEncodeHandler;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

public interface JSONMarshal extends JSONEncoder {

    void setMode( long mode );

    long getMode();

    void setBeanEncoder( BeanJSONEncoder encoder );

    BeanJSONEncoder getBeanEncoder();

    void setEncodeHandlerRegistry( EncodeHandlerRegistry registry );

    EncodeHandlerRegistry getEncodeHandlerRegistry();


    <T> void registerEncodeHandler( Class<T> type, JSONObjectEncodeHandler<? super T> handler );

}
