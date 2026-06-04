package com.pinecone.hydra.device.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public enum DeviceClientTransportType implements Pinenut {
    Husky( 0x01, "Husky" ),  // Husky客户端传输 / Husky Client Transport
    Grpc( 0x02, "gRPC" );    // gRPC客户端传输 / gRPC Client Transport

    private final int code;

    private final String name;

    DeviceClientTransportType( int code, String name ) {
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
