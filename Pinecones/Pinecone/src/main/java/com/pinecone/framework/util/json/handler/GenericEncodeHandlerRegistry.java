package com.pinecone.framework.util.json.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericEncodeHandlerRegistry implements EncodeHandlerRegistry {

    protected final Map<Class<?>, JSONObjectEncodeHandler<?>> mSerializers;

    public GenericEncodeHandlerRegistry() {
        this.mSerializers = new ConcurrentHashMap<>();
    }

    public <T> void register( Class<T> type, JSONObjectEncodeHandler<? super T> serializer ) {
        this.mSerializers.put( type, serializer );
    }

    @SuppressWarnings("unchecked")
    public <T> JSONObjectEncodeHandler<T> get( Class<?> type ) {
        JSONObjectEncodeHandler<?> exact = this.mSerializers.get( type );
        if ( exact != null ) {
            return (JSONObjectEncodeHandler<T>) exact;
        }

        for ( Map.Entry<Class<?>, JSONObjectEncodeHandler<?>> e : this.mSerializers.entrySet() ) {
            if ( e.getKey().isAssignableFrom( type ) ) {
                return (JSONObjectEncodeHandler<T>) e.getValue();
            }
        }

        return null;
    }

}
