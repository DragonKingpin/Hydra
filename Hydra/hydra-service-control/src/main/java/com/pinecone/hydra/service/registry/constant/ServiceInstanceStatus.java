package com.pinecone.hydra.service.registry.constant;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ServiceInstanceStatus implements Pinenut {
    New( 0x01, "New" ),                    // 实例新建 / Instance New
    Registering( 0x02, "Registering" ),    // 实例注册中 / Instance Registering
    Online( 0x03, "Online" ),              // 实例在线 / Instance Online
    Suspect( 0x04, "Suspect" ),            // 实例疑似异常 / Instance Suspect
    Deregistered( 0x05, "Deregistered" ),  // 实例已注销但RPC未断开 / Instance Deregistered while RPC alive
    Terminated( 0x06, "Terminated" ),      // 实例已注销且RPC已断开 / Instance Deregistered and RPC closed
    Expired( 0x07, "Expired" ),            // 实例租约过期 / Instance Lease Expired
    Error( 0x08, "Error" );                // 实例异常 / Instance Error

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
