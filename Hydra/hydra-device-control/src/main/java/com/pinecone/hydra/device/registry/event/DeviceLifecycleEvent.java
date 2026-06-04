package com.pinecone.hydra.device.registry.event;

import com.pinecone.framework.system.prototype.Pinenut;

public enum DeviceLifecycleEvent implements Pinenut {
    Created( 0x01, "Created" ),            // 设备已创建 / Device Created
    Registering( 0x02, "Registering" ),    // 设备注册中 / Device Registering
    Registered( 0x03, "Registered" ),      // 设备已注册 / Device Registered
    Heartbeat( 0x04, "Heartbeat" ),        // 设备心跳 / Device Heartbeat
    Suspected( 0x05, "Suspected" ),        // 设备疑似异常 / Device Suspected
    Deregistered( 0x06, "Deregistered" ),  // 设备已注销 / Device Deregistered
    Offline( 0x07, "Offline" ),            // 设备离线 / Device Offline
    Expired( 0x08, "Expired" ),            // 设备已过期 / Device Expired
    Suspended( 0x09, "Suspended" ),        // 设备已暂停 / Device Suspended
    Retired( 0x0A, "Retired" ),            // 设备已退役 / Device Retired
    Error( 0x0F, "Error" );                // 设备异常 / Device Error

    private final int code;

    private final String name;

    DeviceLifecycleEvent( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static DeviceLifecycleEvent getByCode( int code ) {
        for ( DeviceLifecycleEvent type : DeviceLifecycleEvent.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }

    public static DeviceLifecycleEvent getByName( String name ) {
        for ( DeviceLifecycleEvent type : DeviceLifecycleEvent.values() ) {
            if ( type.name.equals( name ) ) {
                return type;
            }
        }

        return null;
    }
}
