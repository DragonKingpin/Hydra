package com.pinecone.hydra.service.registry.constant;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ServiceStatus implements Pinenut {
    Starting( 0x01, "Starting" ),    // 服务启动中 / Service Starting
    Running( 0x02, "Running" ),      // 服务运行中 / Service Running
    Suspended( 0x03, "Suspended" ),  // 服务已暂停 / Service Suspended
    Stopped( 0x04, "Stopped" ),      // 服务已停止 / Service Stopped
    Error( 0x05, "Error" );          // 服务异常 / Service Error

    private final int code;

    private final String name;

    ServiceStatus( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static ServiceStatus getByCode( int code ) {
        for ( ServiceStatus type : ServiceStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }

    public static ServiceStatus getByName( String name ) {
        for ( ServiceStatus type : ServiceStatus.values() ) {
            if ( type.name.equals( name ) ) {
                return type;
            }
        }

        return null;
    }
}
