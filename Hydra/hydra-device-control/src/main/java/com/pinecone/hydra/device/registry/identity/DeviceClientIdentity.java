package com.pinecone.hydra.device.registry.identity;

import java.nio.ByteBuffer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public final class DeviceClientIdentity implements Pinenut {

    private DeviceClientIdentity() {

    }

    public static long fromDeviceGuid( GUID deviceGuid ) {
        if ( deviceGuid == null ) {
            throw new IllegalArgumentException( "Device guid is required." );
        }

        byte[] bytes = deviceGuid.toBytesBE();
        if ( bytes == null || bytes.length < 16 ) {
            return positive( deviceGuid.hashCode64() );
        }

        ByteBuffer buffer = ByteBuffer.wrap( bytes );
        long high64 = buffer.getLong();
        long low64 = buffer.getLong();
        return positive( high64 ^ low64 );
    }

    protected static long positive( long value ) {
        if ( value == Long.MIN_VALUE ) {
            return Long.MAX_VALUE;
        }
        return Math.abs( value );
    }
}
