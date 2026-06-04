package com.pinecone.hydra.device;

import com.pinecone.framework.system.prototype.Pinenut;

public enum DeviceStatus implements Pinenut {
    New( 0x01, "New" ),                    // 设备新建 / Device New
    Registering( 0x02, "Registering" ),    // 设备注册中 / Device Registering
    Online( 0x03, "Online" ),              // 设备在线 / Device Online
    Suspect( 0x04, "Suspect" ),            // 设备疑似异常 / Device Suspect
    Deregistered( 0x05, "Deregistered" ),  // 设备已注销但RPC未断开 / Device Deregistered while RPC alive
    Offline( 0x06, "Offline" ),            // 设备离线 / Device Offline
    Expired( 0x07, "Expired" ),            // 设备租约过期 / Device Lease Expired
    Suspended( 0x08, "Suspended" ),        // 设备已暂停 / Device Suspended
    Retired( 0x09, "Retired" ),            // 设备已退役 / Device Retired
    Error( 0x0F, "Error" );                // 设备异常 / Device Error

    private final int code;

    private final String name;

    DeviceStatus( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static DeviceStatus getByCode( int code ) {
        for ( DeviceStatus type : DeviceStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }

    public static DeviceStatus getByName( String name ) {
        for ( DeviceStatus type : DeviceStatus.values() ) {
            if ( type.name.equals( name ) ) {
                return type;
            }
        }

        return null;
    }
}
