package com.pinecone.hydra.device.registry.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public enum DeviceControlTransportType implements Pinenut {
    Husky( 0x01, "Husky" ),  // Husky传输 / Husky Transport
    Grpc( 0x02, "gRPC" );    // gRPC传输 / gRPC Transport

    private final int code;

    private final String name;

    DeviceControlTransportType( int code, String name ) {
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
