package com.pinecone.hydra.service.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ServiceClientTransportType implements Pinenut {
    Husky( 0x01, "Husky" ),  // Husky传输 / Husky Transport
    Grpc( 0x02, "Grpc" );    // gRPC传输 / gRPC Transport

    private final int code;

    private final String name;

    ServiceClientTransportType( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static ServiceClientTransportType getByCode( int code ) {
        for ( ServiceClientTransportType type : ServiceClientTransportType.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }

    public static ServiceClientTransportType getByName( String name ) {
        for ( ServiceClientTransportType type : ServiceClientTransportType.values() ) {
            if ( type.name.equals( name ) ) {
                return type;
            }
        }

        return null;
    }
}
