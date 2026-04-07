package com.walnut.archcraft.redstone.response;

public interface RedTraceableResponse extends RedResponse {

    void setRequestId( String requestId );

    String getRequestId();

}
