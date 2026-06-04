package com.device.auto;

import java.util.function.BooleanSupplier;

import com.acorn.skynet.device.conduct.DeviceLegionaryState;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceLegionary;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceLegionaryAssertions implements Pinenut {

    public static void assertNotNull( Object value, String message ) {
        if ( value == null ) {
            throw new IllegalStateException( message );
        }
    }

    public static void assertTrue( boolean value, String message ) {
        if ( !value ) {
            throw new IllegalStateException( message );
        }
    }

    public static void assertOnline( SkyCollectiveDeviceLegionary legionary ) {
        assertNotNull( legionary, "Device legionary is null." );
        assertTrue(
                legionary.getState() == DeviceLegionaryState.Online,
                "Device legionary should be Online, actual => " + legionary.getState()
        );
        assertNotNull( legionary.getInstanceGuid(), "Device legionary instance guid is null." );
    }

    public static void assertTerminated( SkyCollectiveDeviceLegionary legionary ) {
        assertNotNull( legionary, "Device legionary is null." );
        assertTrue(
                legionary.getState() == DeviceLegionaryState.Terminated,
                "Device legionary should be Terminated, actual => " + legionary.getState()
        );
    }

    public static void assertSameInstance( GUID expected, GUID actual ) {
        assertNotNull( expected, "Expected device instance guid is null." );
        assertNotNull( actual, "Actual device instance guid is null." );
        assertTrue( expected.equals( actual ), "Device instance guid should remain stable. expected => " + expected + ", actual => " + actual );
    }

    public static void awaitTrue( String message, long timeoutMillis, BooleanSupplier supplier ) {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while ( System.currentTimeMillis() < deadline ) {
            if ( supplier.getAsBoolean() ) {
                return;
            }
            try {
                Thread.sleep( 100L );
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException( e );
            }
        }
        throw new IllegalStateException( message );
    }
}
