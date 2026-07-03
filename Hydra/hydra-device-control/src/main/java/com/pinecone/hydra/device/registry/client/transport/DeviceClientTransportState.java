package com.pinecone.hydra.device.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public enum DeviceClientTransportState implements Pinenut {
    Closed( 0x00, "Closed" ),  // 传输已关闭 / Transport Closed
    Open( 0x01, "Open" ),      // 传输已打开 / Transport Open
    Error( 0x0F, "Error" );    // 传输异常 / Transport Error

    private final int code;

    private final String name;

    DeviceClientTransportState( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
