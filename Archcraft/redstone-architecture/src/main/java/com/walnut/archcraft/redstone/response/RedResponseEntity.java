package com.walnut.archcraft.redstone.response;

public interface RedResponseEntity<T> extends RedTraceableResponse {

    T getData();

    void setData( T data );

}
