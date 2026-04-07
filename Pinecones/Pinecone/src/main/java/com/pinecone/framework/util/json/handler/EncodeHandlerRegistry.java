package com.pinecone.framework.util.json.handler;

import com.pinecone.framework.system.prototype.Pinenut;

public interface EncodeHandlerRegistry extends Pinenut {

    <T> void register( Class<T> type, JSONObjectEncodeHandler<? super T> serializer );

    <T> JSONObjectEncodeHandler<T> get( Class<?> type );

}
