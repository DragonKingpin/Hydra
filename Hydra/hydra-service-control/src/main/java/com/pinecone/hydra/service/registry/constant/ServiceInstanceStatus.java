package com.pinecone.hydra.service.registry.constant;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ServiceInstanceStatus implements Pinenut {
    New( 0x01, "New" ),                    // 实例新建 / Instance New
    Registering( 0x02, "Registering" ),    // 实例注册中 / Instance Registering
    Online( 0x03, "Online" ),              // 实例在线 / Instance Online
    Detached( 0x04, "Detached" ),          // 控制连接断开，处于宽限观察期 / Control connection detached in grace window
    Suspect( 0x05, "Suspect" ),            // 实例疑似异常 / Instance Suspect
    Deregistered( 0x06, "Deregistered" ),  // 实例已注销但连接仍存在（RPC未断开） / Instance Deregistered while connection may still exist
    Offline( 0x07, "Offline" ),            // 实例离线，注册和连接均已结束（RPC断开） / Instance Offline
    Expired( 0x08, "Expired" ),            // 实例租约过期 / Instance Lease Expired
    Error( 0x09, "Error" );                // 实例异常 / Instance Error

    private final int code;

    private final String name;

    ServiceInstanceStatus( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static ServiceInstanceStatus getByCode( int code ) {
        for ( ServiceInstanceStatus type : ServiceInstanceStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }

    public static ServiceInstanceStatus getByName( String name ) {
        for ( ServiceInstanceStatus type : ServiceInstanceStatus.values() ) {
            if ( type.name.equals( name ) ) {
                return type;
            }
        }

        return null;
    }
}
