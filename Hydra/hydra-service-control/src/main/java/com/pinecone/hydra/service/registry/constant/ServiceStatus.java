package com.pinecone.hydra.service.registry.constant;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ServiceStatus implements Pinenut {
    SERVICE_NEW( 0x00 ),
    SERVICE_RUNNING( 0x01 ), // 服务运行中
    SERVICE_SUSPENDED( 0x02 ), // 服务暂停
    SERVICE_EXISTED( 0x03 ), // 服务存活
    SERVICE_TERMINATED( 0x04 ), // 服务终止（正常结束）
    SERVICE_ERROR( 0x05 ); // 服务终止（因错误结束）

    private final int code;

    ServiceStatus( int code ) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ServiceStatus getByCode( int code ) {
        for ( ServiceStatus type : ServiceStatus.values() ) {
            if ( type.code == code ) {
                return type;
            }
        }

        return null;
    }
}
